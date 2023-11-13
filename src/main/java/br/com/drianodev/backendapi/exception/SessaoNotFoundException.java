package br.com.drianodev.backendapi.exception;

public class SessaoNotFoundException extends RuntimeException {

    public SessaoNotFoundException(String message) {
        super(message);
    }
}
