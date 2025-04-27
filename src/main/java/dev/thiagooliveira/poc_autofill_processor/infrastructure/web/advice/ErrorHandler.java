package dev.thiagooliveira.poc_autofill_processor.infrastructure.web.advice;

import dev.thiagooliveira.poc_autofill_processor.domain.URLGeneratorService;
import dev.thiagooliveira.poc_autofill_processor.domain.exception.DomainException;
import dev.thiagooliveira.poc_autofill_processor.domain.exception.GenericErrorException;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.web.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler  {

    private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(DomainException.class)
    ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request) {
        if(ex.getCause() != null) {
            logger.error(ex.getCause().getMessage(), ex.getCause());
        }
        Error error = new Error(LocalDateTime.now(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
        return super.handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(GenericErrorException.class)
    ResponseEntity<Object> handleGenericErrorException(GenericErrorException ex, WebRequest request) {
        logger.error(ex.getCause().getMessage(), ex.getCause());
        Error error = new Error(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return super.handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
