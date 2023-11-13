package br.com.drianodev.backendapi.service.impl;

import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.exception.PautaNotFoundException;
import br.com.drianodev.backendapi.exception.SessaoNotFoundException;
import br.com.drianodev.backendapi.model.dto.ResultadoVotacaoDTO;
import br.com.drianodev.backendapi.model.dto.SessaoDTO;
import br.com.drianodev.backendapi.model.entity.Pauta;
import br.com.drianodev.backendapi.model.entity.Sessao;
import br.com.drianodev.backendapi.repository.PautaRepository;
import br.com.drianodev.backendapi.repository.SessaoRepository;
import br.com.drianodev.backendapi.service.SessaoService;
import br.com.drianodev.backendapi.service.VotoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SessaoServiceImpl implements SessaoService {

    private static final Logger LOGGER = Logger.getLogger(SessaoServiceImpl.class.getName());

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private VotoService votoService;

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

            // Calcula a data de encerramento da sessão
            LocalDateTime dataFim = sessaoDTO.getDataSessao().plus(duracao);
            sessao.setDataSessao(dataFim);

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

    public void encerrarSessao(Long idSessao) {
        try {
            // Obter a sessão
            Sessao sessao = obterSessao(idSessao);

            // Validar se a sessão ainda está aberta ou expirou
            if (!validarSessao(sessao)) {
                LOGGER.warning("Sessão para votação não está aberta ou expirou.");
                // Sessão para votação não está aberta ou expirou
                return;
            }

            // Encerrar a sessão
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fimSessao = sessao.getDataSessao().plus(sessao.getDuracao());

            if (now.isAfter(fimSessao)) {
                LOGGER.info("Sessão expirou. Encerrando sessão...");

                // Lógica específica para encerrar a sessão quando expirar
                // ...

                // Contabilizar os votos
                ResultadoVotacaoDTO resultadoVotacao = votoService.contabilizarVotos(sessao.getPauta().getId());

                // Enviar mensagem para o Kafka com o resultado
                enviarResultadoVotacao(resultadoVotacao);
            } else {
                LOGGER.info("Sessão ainda está aberta. Não será encerrada neste momento.");
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao encerrar sessão: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    private Sessao obterSessao(Long idSessao) {
        return sessaoRepository.findById(idSessao)
                .orElseThrow(() -> new SessaoNotFoundException("Sessão não encontrada"));
    }

    private boolean validarSessao(Sessao sessao) {
        // Verificar se a sessão está dentro do período de votação
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(sessao.getDataSessao()) && now.isBefore(sessao.getDataSessao().plus(sessao.getDuracao()));
    }

    private void enviarResultadoVotacao(ResultadoVotacaoDTO resultadoVotacao) {
        try {
            kafkaTemplate.send("resultado-votacao-topic", resultadoVotacao);
            LOGGER.info("Resultado da votação enviado para o Kafka.");
        } catch (Exception e) {
            LOGGER.severe("Erro ao enviar resultado da votação para o Kafka: " + e.getMessage());
            // Tratar a exceção conforme necessário
        }
    }
}
