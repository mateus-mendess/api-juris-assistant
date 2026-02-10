package br.com.juristrack.Juris.Track.exception;

public class CpfAlreadyExistsException extends BusinessException {
    public CpfAlreadyExistsException(String cpf) {
        super("CPF already exists in system: " + cpf);
    }
}
