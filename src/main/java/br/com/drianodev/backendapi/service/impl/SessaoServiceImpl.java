package br.com.drianodev.backendapi.service.impl;

import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.exception.PautaNotFoundException;
import br.com.drianodev.backendapi.model.dto.SessaoDTO;
import br.com.drianodev.backendapi.model.entity.Pauta;
import br.com.drianodev.backendapi.model.entity.Sessao;
import br.com.drianodev.backendapi.repository.PautaRepository;
import br.com.drianodev.backendapi.repository.SessaoRepository;
import br.com.drianodev.backendapi.service.SessaoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SessaoServiceImpl implements SessaoService {

    private static final Logger LOGGER = Logger.getLogger(SessaoServiceImpl.class.getName());

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Override
    public SessaoDTO abrirSessao(SessaoDTO sessaoDTO) {
        try {
            Long pautaId = sessaoDTO.getPauta();
            if (pautaId == null) {
                throw new PautaNotFoundException("ID da pauta não pode ser nulo.");
            }

            // Verifica se a pauta existe
            Pauta pauta = pautaRepository.findById(sessaoDTO.getPauta())
                    .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada"));

            // Se a duração não for especificada, utiliza o valor padrão de 1 minuto
            Duration duracao = (sessaoDTO.getDuracao() != null) ? sessaoDTO.getDuracao() : Duration.ofMinutes(1);

            // Cria a entidade Sessao a partir do DTO
            Sessao sessao = new Sessao();
            sessao.setPauta(pauta);
            sessao.setDataSessao(sessaoDTO.getDataSessao());
            sessao.setDuracao(duracao);

            // Salva a sessão
            sessao = sessaoRepository.save(sessao);

            // Mapeamento manual
            SessaoDTO resultDTO = new SessaoDTO();
            resultDTO.setId(sessao.getId());
            resultDTO.setPauta(sessao.getPauta().getId()); // ou algo similar dependendo da estrutura de PautaDTO
            resultDTO.setDataSessao(sessao.getDataSessao());
            resultDTO.setDuracao(sessao.getDuracao());

            return resultDTO;
        } catch (Exception e) {
            LOGGER.severe("Erro ao abrir sessão: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    private SessaoDTO mapSessaoToDTO(Sessao sessao) {
        SessaoDTO sessaoDTO = new SessaoDTO();
        sessaoDTO.setId(sessao.getId());
        sessaoDTO.setPauta(sessao.getPauta().getId());
        sessaoDTO.setDataSessao(sessao.getDataSessao());
        sessaoDTO.setDuracao(sessao.getDuracao());
        return sessaoDTO;
    }

    private List<SessaoDTO> mapSessoesToDTOs(List<Sessao> sessoes) {
        return sessoes.stream()
                .map(this::mapSessaoToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessaoDTO> listarSessoes() {
        try {
            List<Sessao> sessoes = sessaoRepository.findAll();
            return mapSessoesToDTOs(sessoes);
        } catch (Exception e) {
            LOGGER.severe("Erro ao listar sessões: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    @Override
    public SessaoDTO buscarSessaoPorId(Long id) {
        try {
            Sessao sessao = sessaoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Sessão não encontrada"));
            return mapSessaoToDTO(sessao);
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar sessão por ID: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }
}
