package br.com.drianodev.backendapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SessaoDTO {

    private Long id;
    private Long pauta;
    private LocalDateTime dataSessao;
    private Duration duracao;
}
