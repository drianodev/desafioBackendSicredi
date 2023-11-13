package br.com.drianodev.backendapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCadastrarAssociado() throws Exception {
        String associadoJSON = "{ \"nome\": \"Teste\", \"cpf\": \"65270302071\" }";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/associado")
                        .content(associadoJSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Api-Version", "1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testListarAssociados() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/associado")
                        .header("Api-Version", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBuscarAssociadoPorId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/associado/1")
                        .header("Api-Version", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testVerificarHabilitacaoVotoPorCpf() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/associado/verificar-habilitacao-voto/65270302071")
                        .header("Api-Version", "1"))
                .andExpect(status().isOk());
    }
}
