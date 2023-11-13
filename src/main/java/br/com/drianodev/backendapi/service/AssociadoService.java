package br.com.drianodev.backendapi.service;

import br.com.drianodev.backendapi.model.dto.AssociadoDTO;

import java.util.List;

public interface AssociadoService {

    AssociadoDTO cadastrarAssociado(AssociadoDTO associadoDTO);
    List<AssociadoDTO> listarAssociados();
    AssociadoDTO buscarAssociadoPorId(Long id);
    AssociadoDTO buscarAssociadoPorCpf(String cpf);
    String verificarHabilitacaoVoto(String cpf);
}
