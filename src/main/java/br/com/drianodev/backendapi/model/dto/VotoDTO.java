package br.com.drianodev.backendapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VotoDTO {

    private Long id;
    private Long idAssociado;
    private String cpfAssociado;
    private Long idPauta;
    private boolean voto;
}
