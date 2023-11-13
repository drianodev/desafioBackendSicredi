package br.com.drianodev.backendapi.service;

import br.com.drianodev.backendapi.model.dto.ResultadoVotacaoDTO;
import br.com.drianodev.backendapi.model.dto.VotoDTO;

import java.util.List;

public interface VotoService {

    VotoDTO votar(VotoDTO votoDTO);
    List<VotoDTO> buscarTodosVotosPorPauta(Long idPauta);
    public ResultadoVotacaoDTO contabilizarVotos(Long idPauta);
}
