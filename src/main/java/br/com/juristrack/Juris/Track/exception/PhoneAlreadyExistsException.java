package br.com.juristrack.Juris.Track.exception;

public class PhoneAlreadyExistsException extends BusinessException {
    public PhoneAlreadyExistsException(String phone, String field) {
        super("Phone already registered: " + phone, field);
    }
}
