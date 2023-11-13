package br.com.drianodev.backendapi.model.event;

import lombok.Data;

@Data
public class ResultadoVotacaoEvent {

    private Long idPauta;
    private int votosSim;
    private int votosNao;
}
