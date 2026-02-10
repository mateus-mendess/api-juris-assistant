package br.com.juristrack.Juris.Track.exception;

public class PhoneAlreadyExistsException extends BusinessException {
    public PhoneAlreadyExistsException(String phone) {
        super("Phone already registered: " + phone);
    }
}
