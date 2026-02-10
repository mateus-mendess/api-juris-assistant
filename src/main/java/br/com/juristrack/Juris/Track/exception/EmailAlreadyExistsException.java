package br.com.juristrack.Juris.Track.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists in system: " + email);
    }
}
