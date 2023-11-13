package br.com.drianodev.backendapi.service;

import br.com.drianodev.backendapi.model.dto.PautaDTO;

import java.util.List;

public interface PautaService {

    PautaDTO cadastrarPauta(PautaDTO pautaDTO);
    List<PautaDTO> listarPautas();
    PautaDTO buscarPautaPorId(Long id);
}
