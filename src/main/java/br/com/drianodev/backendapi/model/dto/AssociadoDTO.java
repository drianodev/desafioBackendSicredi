package br.com.drianodev.backendapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AssociadoDTO {

    private Long id;
    private String nome;
    private String cpf;
}
