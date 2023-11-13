package br.com.drianodev.backendapi.service.impl;

import br.com.drianodev.backendapi.exception.PautaNotFoundException;
import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.model.dto.ResultadoVotacaoDTO;
import br.com.drianodev.backendapi.model.dto.VotoDTO;
import br.com.drianodev.backendapi.model.entity.Pauta;
import br.com.drianodev.backendapi.model.entity.Sessao;
import br.com.drianodev.backendapi.model.entity.Voto;
import br.com.drianodev.backendapi.repository.PautaRepository;
import br.com.drianodev.backendapi.repository.SessaoRepository;
import br.com.drianodev.backendapi.repository.VotoRepository;
import br.com.drianodev.backendapi.service.AssociadoService;
import br.com.drianodev.backendapi.service.VotoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class VotoServiceImpl implements VotoService {

    private static final Logger LOGGER = Logger.getLogger(VotoServiceImpl.class.getName());

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Override
    public VotoDTO votar(VotoDTO votoDTO) {
        System.out.println("VotoDTO " + votoDTO);
        try {
            AssociadoDTO associadoDTO = associadoService.buscarAssociadoPorCpf(votoDTO.getCpfAssociado());
            if (associadoDTO == null) {
                LOGGER.warning("Associado não encontrado ao votar.");
                return null;
            }

            if (votoRepository.existsByCpfAssociadoAndPautaId(votoDTO.getCpfAssociado(), votoDTO.getIdPauta())) {
                LOGGER.warning("Associado já votou nesta pauta ao votar.");
                return null;
            }

            if (!validarSessaoParaVotacao(votoDTO.getIdPauta())) {
                LOGGER.warning("Sessão para votação não está aberta ou expirou.");
                return null;
            }

            Voto voto = mapperUtils.convert(votoDTO, Voto.class);

            voto.setIdAssociado(associadoDTO.getId());
            voto.setCpfAssociado(associadoDTO.getCpf());

            Voto savedVoto = votoRepository.save(voto);
            return mapperUtils.convert(savedVoto, VotoDTO.class);
        } catch (Exception e) {
            LOGGER.severe("Erro ao votar: " + e.getMessage());
            throw e;
        }
    }

    private boolean validarSessaoParaVotacao(Long idPauta) {
        Optional<Sessao> optionalSessao = sessaoRepository.findByPauta_Id(idPauta);

        if (optionalSessao.isPresent()) {
            Sessao sessao = optionalSessao.get();

            if (!sessao.getPauta().getId().equals(idPauta)) {
                LOGGER.warning("Tentativa de votar em uma pauta diferente da associada à sessão.");
                return false;
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime inicioSessao = sessao.getDataSessao();
            LocalDateTime fimSessao = sessao.getDataSessao().plus(sessao.getDuracao());

            if (now.isBefore(inicioSessao)) {
                LOGGER.warning("Sessão ainda não começou. Início: " + inicioSessao);
                return false;
            }

            if (now.isAfter(fimSessao)) {
                LOGGER.warning("Sessão já expirou. Fim: " + fimSessao);
                return false;
            }

            return true;
        }

        LOGGER.warning("Sessão não encontrada para a pauta com ID: " + idPauta);
        return false;
    }

    @Override
    public List<VotoDTO> buscarTodosVotosPorPauta(Long idPauta) {
        try {
            List<Voto> votos = votoRepository.findAllByPautaId(idPauta);
            return mapperUtils.convertList(votos, VotoDTO.class);
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar votos por pauta: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ResultadoVotacaoDTO contabilizarVotos(Long idPauta) {
        try {
            Pauta pauta = pautaRepository.findById(idPauta)
                    .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada"));

            List<Voto> votos = votoRepository.findAllByPautaId(idPauta);

            long votosFavoraveis = votos.stream().filter(voto -> voto.getVoto()).count();

            long votosContrarios = votos.stream().filter(voto -> !voto.getVoto()).count();

            long totalVotos = votos.size();

            ResultadoVotacaoDTO resultado = new ResultadoVotacaoDTO();
            resultado.setIdPauta(idPauta);
            resultado.setVotosFavoraveis(votosFavoraveis);
            resultado.setVotosContrarios(votosContrarios);
            resultado.setTotalVotos(totalVotos);

            return resultado;
        } catch (Exception e) {
            LOGGER.severe("Erro ao contabilizar votos: " + e.getMessage());
            throw e;
        }
    }

}
