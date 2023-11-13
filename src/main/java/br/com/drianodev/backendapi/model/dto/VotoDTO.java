package br.com.drianodev.backendapi.model.dto;

import br.com.drianodev.backendapi.model.dto.deserializer.BooleanAsStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VotoDTO {

    private Long id;
    private Long idAssociado;
    private String cpfAssociado;
    private Long idPauta;

    @JsonDeserialize(using = BooleanAsStringDeserializer.class)
    private boolean voto;
}
