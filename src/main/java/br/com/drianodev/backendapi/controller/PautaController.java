package br.com.drianodev.backendapi.controller;

import br.com.drianodev.backendapi.exception.NotFoundException;
import br.com.drianodev.backendapi.model.dto.PautaDTO;
import br.com.drianodev.backendapi.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    private static final Logger LOGGER = Logger.getLogger(PautaController.class.getName());

    @Autowired
    private PautaService pautaService;

    @PostMapping(headers = "Api-Version=1")
    public ResponseEntity<PautaDTO> cadastrarPauta(@RequestBody PautaDTO pautaDTO) {
        try {
            PautaDTO novaPauta = pautaService.cadastrarPauta(pautaDTO);
            if (novaPauta != null) {
                return new ResponseEntity<>(novaPauta, HttpStatus.CREATED);
            } else {
                LOGGER.warning("Falha ao cadastrar pauta. PautaDTO nula.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            LOGGER.warning("Associado não encontrado ao cadastrar pauta: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.severe("Erro ao cadastrar pauta: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(headers = "Api-Version=1")
    public ResponseEntity<List<PautaDTO>> listarPautas() {
        try {
            List<PautaDTO> pautas = pautaService.listarPautas();
            return new ResponseEntity<>(pautas, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe("Erro ao listar pautas: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/buscar/{id}", headers = "Api-Version=1")
    public ResponseEntity<PautaDTO> buscarPautaPorId(@PathVariable Long id) {
        try {
            PautaDTO pauta = pautaService.buscarPautaPorId(id);
            if (pauta != null) {
                return new ResponseEntity<>(pauta, HttpStatus.OK);
            } else {
                LOGGER.warning("Pauta não encontrada com o ID: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar pauta por ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}