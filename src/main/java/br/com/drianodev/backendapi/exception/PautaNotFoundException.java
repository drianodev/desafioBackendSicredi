package br.com.drianodev.backendapi.exception;

public class PautaNotFoundException extends RuntimeException {

    public PautaNotFoundException(String message) {
        super(message);
    }

    public PautaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
