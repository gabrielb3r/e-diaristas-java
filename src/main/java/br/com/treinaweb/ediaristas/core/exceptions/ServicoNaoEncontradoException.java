package br.com.treinaweb.ediaristas.core.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class ServicoNaoEncontradoException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public ServicoNaoEncontradoException(String message) {
        super(message);
    }
    
}

