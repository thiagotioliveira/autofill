package dev.thiagooliveira.poc_autofill_processor.domain.exception;

public class GenericErrorException extends RuntimeException {

    public GenericErrorException(Throwable cause) {
        super("generic error", cause);
    }
}
