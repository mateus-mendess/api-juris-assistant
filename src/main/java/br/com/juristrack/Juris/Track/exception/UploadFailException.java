package br.com.juristrack.Juris.Track.exception;

public class UploadFailException extends RuntimeException {
    public UploadFailException(String message) {
        super(message);
    }
}
