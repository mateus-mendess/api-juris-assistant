package br.com.juristrack.Juris.Track.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final String field;

    public BusinessException(String message, String field) {
        super(message);
        this.field = field;
    }
}
