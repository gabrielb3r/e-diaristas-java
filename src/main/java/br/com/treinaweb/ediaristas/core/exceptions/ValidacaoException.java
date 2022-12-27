package br.com.treinaweb.ediaristas.core.exceptions;

import org.springframework.validation.FieldError;

public class ValidacaoException extends RuntimeException {

    private FieldError fieldError;

    public ValidacaoException(String arg0, FieldError fieldError) {
        super(arg0);
        this.fieldError = fieldError;
    }

    public FieldError getFieldError() {
        return fieldError;
    }

}
