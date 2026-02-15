package br.com.juristrack.Juris.Track.handler;

import br.com.juristrack.Juris.Track.dto.response.ErrorResponse;
import br.com.juristrack.Juris.Track.exception.BusinessException;
import br.com.juristrack.Juris.Track.exception.FileStorageException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                400,
                exception.getMessage(),
                exception.getField(),
                "This type of data already exists in the system:" + exception.getField()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                404,
                exception.getMessage(),
                null,
                "Information not found in the system."
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException exception) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                500,
                null,
                exception.getMessage(),
                "Error handling file/images in the system, please try again in a few minutes."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
