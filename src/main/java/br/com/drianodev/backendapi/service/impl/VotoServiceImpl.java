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
            // Verificar se o associado com o CPF fornecido existe
            AssociadoDTO associadoDTO = associadoService.buscarAssociadoPorCpf(votoDTO.getCpfAssociado());
            if (associadoDTO == null) {
                LOGGER.warning("Associado não encontrado ao votar.");
                // Associado não encontrado
                return null;
            }

            // Verificar se o associado já votou nesta pauta
            if (votoRepository.existsByCpfAssociadoAndPautaId(votoDTO.getCpfAssociado(), votoDTO.getIdPauta())) {
                LOGGER.warning("Associado já votou nesta pauta ao votar.");
                // Associado já votou nesta pauta
                return null;
            }

            // Verificar se a sessão para votação está aberta
            if (!validarSessaoParaVotacao(votoDTO.getIdPauta())) {
                LOGGER.warning("Sessão para votação não está aberta ou expirou.");
                // Sessão para votação não está aberta ou expirou
                return null;
            }

            // Mapear VotoDTO para Voto
            Voto voto = mapperUtils.convert(votoDTO, Voto.class);

            // Configurar a referência ao associado
            voto.setIdAssociado(associadoDTO.getId());
            voto.setCpfAssociado(associadoDTO.getCpf());

            // Salvar o voto
            Voto savedVoto = votoRepository.save(voto);

            // Mapear o Voto salvo de volta para VotoDTO
            return mapperUtils.convert(savedVoto, VotoDTO.class);
        } catch (Exception e) {
            LOGGER.severe("Erro ao votar: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    private boolean validarSessaoParaVotacao(Long idPauta) {
        // Obter a sessão da pauta
        Optional<Sessao> optionalSessao = sessaoRepository.findByPauta_Id(idPauta);

        if (optionalSessao.isPresent()) {
            Sessao sessao = optionalSessao.get();

            // Verificar se a sessão está dentro do período de votação
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(sessao.getDataSessao()) || now.isAfter(sessao.getDataSessao().plus(sessao.getDuracao()))) {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public List<VotoDTO> buscarTodosVotosPorPauta(Long idPauta) {
        try {
            List<Voto> votos = votoRepository.findAllByPautaId(idPauta);
            return mapperUtils.convertList(votos, VotoDTO.class);
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar votos por pauta: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    @Override
    public ResultadoVotacaoDTO contabilizarVotos(Long idPauta) {
        try {
            // Verificar se a pauta existe
            Pauta pauta = pautaRepository.findById(idPauta)
                    .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada"));

            // Buscar todos os votos para esta pauta
            List<Voto> votos = votoRepository.findAllByPautaId(idPauta);

            // Contar os votos favoráveis (true)
            long votosFavoraveis = votos.stream().filter(voto -> voto.getVoto()).count();

            // Contar os votos contrários (false)
            long votosContrarios = votos.stream().filter(voto -> !voto.getVoto()).count();

            // Total de votos
            long totalVotos = votos.size();

            // Criar o resultado
            ResultadoVotacaoDTO resultado = new ResultadoVotacaoDTO();
            resultado.setIdPauta(idPauta);
            resultado.setVotosFavoraveis(votosFavoraveis);
            resultado.setVotosContrarios(votosContrarios);
            resultado.setTotalVotos(totalVotos);

            return resultado;
        } catch (Exception e) {
            LOGGER.severe("Erro ao contabilizar votos: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

}
