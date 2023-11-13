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
            AssociadoDTO associadoDTO = associadoService.buscarAssociadoPorCpf(pautaDTO.getCpfAssociado());

            Pauta pauta = mapperUtils.convert(pautaDTO, Pauta.class);

            Associado associado = mapperUtils.convert(associadoDTO, Associado.class);
            pauta.setAssociado(associado);

            Pauta savedPauta = pautaRepository.save(pauta);
            return mapperUtils.convert(savedPauta, PautaDTO.class);
        } catch (NotFoundException e) {
            LOGGER.warning("Associado não encontrado ao cadastrar pauta: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Erro ao cadastrar pauta: " + e.getMessage());
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
