package br.com.treinaweb.ediaristas.core.exceptions;

import org.springframework.validation.FieldError;

public class UsuarioJaCadastradoException extends ValidacaoException {

    public UsuarioJaCadastradoException(String arg0, FieldError fieldError) {
        super(arg0, fieldError);
    }
    
}
