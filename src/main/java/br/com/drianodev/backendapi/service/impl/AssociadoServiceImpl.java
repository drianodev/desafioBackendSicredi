package br.com.drianodev.backendapi.service.impl;

import br.com.drianodev.backendapi.exception.CpfInvalidoException;
import br.com.drianodev.backendapi.exception.HabilitacaoVotoException;
import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.model.entity.Associado;
import br.com.drianodev.backendapi.model.entity.ValidacaoCpfResponse;
import br.com.drianodev.backendapi.repository.AssociadoRepository;
import br.com.drianodev.backendapi.service.AssociadoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    private static final Logger LOGGER = Logger.getLogger(AssociadoServiceImpl.class.getName());

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AssociadoDTO cadastrarAssociado(AssociadoDTO associadoDTO) {
        try {
            String cpf = associadoDTO.getCpf();
            String resultadoVerificacao = verificarHabilitacaoVoto(cpf);

            if ("ABLE_TO_VOTE".equals(resultadoVerificacao)) {
                Associado associado = mapperUtils.convert(associadoDTO, Associado.class);
                Associado savedAssociado = associadoRepository.save(associado);
                return mapperUtils.convert(savedAssociado, AssociadoDTO.class);
            } else {
                throw new CpfInvalidoException("CPF inválido ou não habilitado para voto");
            }
        } catch (Exception e) {
            LOGGER.warning("Erro ao cadastrar associado: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<AssociadoDTO> listarAssociados() {
        List<Associado> associados = associadoRepository.findAll();
        return mapperUtils.convertList(associados, AssociadoDTO.class);
    }

    @Override
    public AssociadoDTO buscarAssociadoPorId(Long id) {
        Optional<Associado> optionalAssociado = associadoRepository.findById(id);
        return optionalAssociado.map(associado -> mapperUtils.convert(associado, AssociadoDTO.class))
                .orElseThrow(() -> new NotFoundException("Associado não encontrado com o ID: " + id));
    }

    @Override
    public AssociadoDTO buscarAssociadoPorCpf(String cpf) {
        Optional<Associado> associado = associadoRepository.findByCpf(cpf);
        return associado.map(entity -> mapperUtils.convert(entity, AssociadoDTO.class)).orElse(null);
    }

    @Override
    public String verificarHabilitacaoVoto(String cpf) {
        String url = "https://api-validador-cpf.vercel.app/validarcpf/" + cpf;
        try {
            ResponseEntity<ValidacaoCpfResponse> response = restTemplate.getForEntity(url, ValidacaoCpfResponse.class);

            if (response.getBody() != null) {
                boolean cpfValido = response.getBody().isValid();

                if (cpfValido) {
                    return "ABLE_TO_VOTE";
                } else {
                    throw new CpfInvalidoException("CPF não habilitado para voto");
                }
            }
        } catch (HttpClientErrorException.NotFound notFoundException) {
            throw new CpfInvalidoException("CPF não encontrado no validador externo");
        } catch (Exception e) {
            LOGGER.warning("Erro ao verificar habilitação de voto para o CPF " + cpf + ": " + e.getMessage());
            throw new HabilitacaoVotoException("Erro durante a verificação de habilitação de voto");
        }
        return "UNABLE_TO_VOTE";
    }
}
