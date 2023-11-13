package br.com.drianodev.backendapi.controller;

import br.com.drianodev.backendapi.model.dto.ResultadoVotacaoDTO;
import br.com.drianodev.backendapi.model.dto.VotoDTO;
import br.com.drianodev.backendapi.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/voto")
public class VotoController {

    private static final Logger LOGGER = Logger.getLogger(VotoController.class.getName());

    @Autowired
    private VotoService votoService;

    @PostMapping(headers = "Api-Version=1")
    public ResponseEntity<VotoDTO> votar(@RequestBody VotoDTO votoDTO) {
        try {
            VotoDTO novoVoto = votoService.votar(votoDTO);
            if (novoVoto != null) {
                return new ResponseEntity<>(novoVoto, HttpStatus.CREATED);
            } else {
                LOGGER.warning("Falha ao processar o voto. Verifique se o associado existe ou se já votou nesta pauta.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao processar o voto: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/pauta/{idPauta}", headers = "Api-Version=1")
    public ResponseEntity<List<VotoDTO>> buscarTodosVotosPorPauta(@PathVariable Long idPauta) {
        try {
            List<VotoDTO> votos = votoService.buscarTodosVotosPorPauta(idPauta);
            return new ResponseEntity<>(votos, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar votos por pauta: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/resultado/{idPauta}", headers = "Api-Version=1")
    public ResponseEntity<ResultadoVotacaoDTO> obterResultadoVotacao(@PathVariable Long idPauta) {
        try {
            ResultadoVotacaoDTO resultado = votoService.contabilizarVotos(idPauta);
            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao obter resultado da votação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
