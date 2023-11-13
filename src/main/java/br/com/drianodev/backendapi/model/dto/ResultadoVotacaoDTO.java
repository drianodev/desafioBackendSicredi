package br.com.drianodev.backendapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResultadoVotacaoDTO {

    private Long idPauta;
    private Long votosFavoraveis;
    private Long votosContrarios;
    private Long totalVotos;
}
