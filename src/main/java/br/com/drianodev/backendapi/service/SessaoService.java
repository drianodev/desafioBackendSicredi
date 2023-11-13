package br.com.drianodev.backendapi.service;

import br.com.drianodev.backendapi.model.dto.SessaoDTO;

import java.util.List;

public interface SessaoService {

    SessaoDTO abrirSessao(SessaoDTO sessaoDTO);
    List<SessaoDTO> listarSessoes();
    SessaoDTO buscarSessaoPorId(Long id);
}
