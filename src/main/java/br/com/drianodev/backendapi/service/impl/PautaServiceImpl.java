package br.com.drianodev.backendapi.service.impl;

import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.model.dto.PautaDTO;
import br.com.drianodev.backendapi.model.entity.Associado;
import br.com.drianodev.backendapi.model.entity.Pauta;
import br.com.drianodev.backendapi.repository.PautaRepository;
import br.com.drianodev.backendapi.service.AssociadoService;
import br.com.drianodev.backendapi.service.PautaService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PautaServiceImpl implements PautaService {

    private static final Logger LOGGER = Logger.getLogger(PautaServiceImpl.class.getName());

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private MapperUtils mapperUtils;

    @Autowired
    private AssociadoService associadoService;

    @Override
    public PautaDTO cadastrarPauta(PautaDTO pautaDTO) {
        try {
            // Verifica se o associado com o CPF fornecido existe
            AssociadoDTO associadoDTO = associadoService.buscarAssociadoPorCpf(pautaDTO.getCpfAssociado());

            // Mapeia a PautaDTO para a entidade Pauta
            Pauta pauta = mapperUtils.convert(pautaDTO, Pauta.class);

            // Mapeia o AssociadoDTO para a entidade Associado e associa à Pauta
            Associado associado = mapperUtils.convert(associadoDTO, Associado.class);
            pauta.setAssociado(associado);

            // Salva a Pauta no repositório
            Pauta savedPauta = pautaRepository.save(pauta);

            // Converte a entidade salva para DTO e retorna
            return mapperUtils.convert(savedPauta, PautaDTO.class);
        } catch (NotFoundException e) {
            LOGGER.warning("Associado não encontrado ao cadastrar pauta: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Erro ao cadastrar pauta: " + e.getMessage());
            // Propaga a exceção para tratamento adequado no controlador, se necessário
            throw e;
        }
    }

    @Override
    public List<PautaDTO> listarPautas() {
        List<Pauta> pautas = pautaRepository.findAll();
        return mapperUtils.convertList(pautas, PautaDTO.class);
    }

    @Override
    public PautaDTO buscarPautaPorId(Long id) {
        Optional<Pauta> optionalPauta = pautaRepository.findById(id);
        return optionalPauta.map(pauta -> mapperUtils.convert(pauta, PautaDTO.class)).orElse(null);
    }
}
