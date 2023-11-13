package br.com.drianodev.backendapi.controller;

import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.exception.PautaNotFoundException;
import br.com.drianodev.backendapi.model.dto.SessaoDTO;
import br.com.drianodev.backendapi.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/sessao")
public class SessaoController {

    private static final Logger LOGGER = Logger.getLogger(SessaoController.class.getName());

    @Autowired
    private SessaoService sessaoService;

    @PostMapping(headers = "Api-Version=1")
    public ResponseEntity<SessaoDTO> abrirSessao(@RequestBody SessaoDTO sessaoDTO) {
        try {
            SessaoDTO novaSessao = sessaoService.abrirSessao(sessaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaSessao);
        } catch (PautaNotFoundException e) {
            LOGGER.warning("Falha ao abrir sessão: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            LOGGER.severe("Erro ao abrir sessão: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(headers = "Api-Version=1")
    public ResponseEntity<List<SessaoDTO>> listarSessoes() {
        try {
            List<SessaoDTO> sessoes = sessaoService.listarSessoes();
            return ResponseEntity.ok(sessoes);
        } catch (Exception e) {
            LOGGER.severe("Erro ao listar sessões: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{id}", headers = "Api-Version=1")
    public ResponseEntity<SessaoDTO> buscarSessaoPorId(@PathVariable Long id) {
        try {
            SessaoDTO sessao = sessaoService.buscarSessaoPorId(id);
            return ResponseEntity.ok(sessao);
        } catch (NotFoundException e) {
            LOGGER.warning("Sessão não encontrada: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar sessão por ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
