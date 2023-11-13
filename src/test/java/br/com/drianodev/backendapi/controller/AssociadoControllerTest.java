package br.com.drianodev.backendapi.controller;

import br.com.drianodev.backendapi.model.dto.AssociadoDTO;
import br.com.drianodev.backendapi.service.AssociadoService;
import br.com.drianodev.backendapi.utils.MapperUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssociadoControllerTest {

    @Mock
    private AssociadoService associadoService;

    @Mock
    private MapperUtils mapperUtils;

    @InjectMocks
    private AssociadoController associadoController;

    @Test
    void cadastrarAssociado_Success() {
        // Arrange
        AssociadoDTO associadoDTO = new AssociadoDTO();
        AssociadoDTO savedAssociadoDTO = new AssociadoDTO();
        when(associadoService.cadastrarAssociado(any(AssociadoDTO.class))).thenReturn(savedAssociadoDTO);

        // Act
        ResponseEntity<AssociadoDTO> responseEntity = associadoController.cadastrarAssociado(associadoDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedAssociadoDTO, responseEntity.getBody());
    }

    @Test
    void cadastrarAssociado_Failure() {
        // Arrange
        AssociadoDTO associadoDTO = new AssociadoDTO();
        when(associadoService.cadastrarAssociado(any(AssociadoDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<AssociadoDTO> responseEntity = associadoController.cadastrarAssociado(associadoDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void listarAssociados() {
        // Arrange
        List<AssociadoDTO> associados = Arrays.asList(new AssociadoDTO(), new AssociadoDTO());
        when(associadoService.listarAssociados()).thenReturn(associados);

        // Act
        ResponseEntity<List<AssociadoDTO>> responseEntity = associadoController.listarAssociados();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(associados, responseEntity.getBody());
    }

    @Test
    void buscarAssociadoPorId_Success() {
        // Arrange
        Long associadoId = 1L;
        AssociadoDTO associadoDTO = new AssociadoDTO();
        when(associadoService.buscarAssociadoPorId(anyLong())).thenReturn(associadoDTO);

        // Act
        ResponseEntity<AssociadoDTO> responseEntity = associadoController.buscarAssociadoPorId(associadoId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(associadoDTO, responseEntity.getBody());
    }

    @Test
    void buscarAssociadoPorId_Failure() {
        // Arrange
        Long associadoId = 1L;
        when(associadoService.buscarAssociadoPorId(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<AssociadoDTO> responseEntity = associadoController.buscarAssociadoPorId(associadoId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
