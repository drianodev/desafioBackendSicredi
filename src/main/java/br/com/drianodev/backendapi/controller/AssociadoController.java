package br.com.drianodev.backendapi.controller;

import br.com.drianodev.backendapi.exception.*;
import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.service.AssociadoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/associado")
public class AssociadoController {

    private static final Logger LOGGER = Logger.getLogger(AssociadoController.class.getName());

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private MapperUtils mapperUtils;

    @PostMapping(headers = "Api-Version=1")
    public ResponseEntity<AssociadoDTO> cadastrarAssociado(@RequestBody AssociadoDTO associadoDTO) {
        try {
            AssociadoDTO novoAssociado = associadoService.cadastrarAssociado(associadoDTO);
            return new ResponseEntity<>(novoAssociado, HttpStatus.CREATED);
        } catch (CpfInvalidoException e) {
            LOGGER.warning("Tentativa de cadastrar associado com CPF inválido: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AssociadoExistenteException e) {
            LOGGER.warning("Tentativa de cadastrar associado com CPF já existente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT); // HTTP 409 Conflict
        } catch (Exception e) {
            LOGGER.warning("Erro ao cadastrar associado: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(headers = "Api-Version=1")
    public ResponseEntity<List<AssociadoDTO>> listarAssociados() {
        try {
            List<AssociadoDTO> associados = associadoService.listarAssociados();
            return new ResponseEntity<>(associados, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe("Erro ao listar associados: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", headers = "Api-Version=1")
    public ResponseEntity<AssociadoDTO> buscarAssociadoPorId(@PathVariable Long id) {
        try {
            AssociadoDTO associado = associadoService.buscarAssociadoPorId(id);
            return new ResponseEntity<>(associado, HttpStatus.OK);
        } catch (NotFoundException e) {
            LOGGER.warning("Associado não encontrado com o ID: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.warning("Erro ao buscar associado por ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verificar-habilitacao-voto/{cpf}")
    public ResponseEntity<String> verificarHabilitacaoVotoPorCpf(@PathVariable String cpf) {
        try {
            String status = associadoService.verificarHabilitacaoVoto(cpf);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (CpfInvalidoException e) {
            LOGGER.warning("CPF inválido ao verificar habilitação de voto: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HabilitacaoVotoException e) {
            LOGGER.warning("Erro ao verificar habilitação de voto para o CPF " + cpf + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.warning("Erro desconhecido ao verificar habilitação de voto para o CPF " + cpf + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
