package br.com.juristrack.Juris.Track.exception;

public class FileRequiredException extends RuntimeException {
    public FileRequiredException(String message) {
        super(message);
    }
}
