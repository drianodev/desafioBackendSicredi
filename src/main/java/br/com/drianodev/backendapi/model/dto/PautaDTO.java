package br.com.drianodev.backendapi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PautaDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String cpfAssociado;
}
