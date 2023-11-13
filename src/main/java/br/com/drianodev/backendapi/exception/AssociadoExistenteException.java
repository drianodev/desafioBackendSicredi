package br.com.drianodev.backendapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AssociadoExistenteException extends RuntimeException {

    public AssociadoExistenteException(String message) {
        super(message);
    }
}
