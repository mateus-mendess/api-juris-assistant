package br.com.juristrack.Juris.Track.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private String field;

    public BusinessException(String message, String field) {
        super(message);
        this.field = field;
    }
}
