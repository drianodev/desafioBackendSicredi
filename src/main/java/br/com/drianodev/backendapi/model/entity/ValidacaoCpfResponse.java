package br.com.drianodev.backendapi.model.entity;

import lombok.Data;

@Data
public class ValidacaoCpfResponse {

    private boolean valid;
    private String message;
}
