package br.com.drianodev.backendapi.service;

import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.service.impl.AssociadoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AssociadoServiceImplTest {

    @Autowired
    private AssociadoServiceImpl associadoService;

//    @Test
//    public void testCadastrarAssociado() {
//        AssociadoDTO associadoDTO = new AssociadoDTO();
//        associadoDTO.setNome("Teste");
//        associadoDTO.setCpf("12345678900");
//
//        assertDoesNotThrow(() -> associadoService.cadastrarAssociado(associadoDTO));
//    }

    @Test
    public void testListarAssociados() {
        assertNotNull(associadoService.listarAssociados());
    }

    @Test
    public void testBuscarAssociadoPorId() {
        assertDoesNotThrow(() -> associadoService.buscarAssociadoPorId(1L));
    }

//    @Test
//    public void testBuscarAssociadoPorCpf() {
//        assertNotNull(associadoService.buscarAssociadoPorCpf("12345678900"));
//    }
//
//    @Test
//    public void testVerificarHabilitacaoVoto() {
//        assertDoesNotThrow(() -> associadoService.verificarHabilitacaoVoto("12345678900"));
//    }
}

