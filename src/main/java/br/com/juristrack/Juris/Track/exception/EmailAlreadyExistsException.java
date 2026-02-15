package br.com.juristrack.Juris.Track.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email, String field) {
        super("Email already exists in system: " + email, field);
    }
}
