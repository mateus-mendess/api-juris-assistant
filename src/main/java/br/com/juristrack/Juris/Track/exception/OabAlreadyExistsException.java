package br.com.juristrack.Juris.Track.exception;

public class OabAlreadyExistsException extends BusinessException {
    public OabAlreadyExistsException(String oab, String state) {
        super("OAB already registered: " + oab + "/" + state);
    }
}
