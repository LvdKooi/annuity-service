package nl.kooi.api;

import nl.kooi.api.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto().reason(exception.getMessage()).reference(UUID.randomUUID()));
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<ErrorResponseDto> handleError(Error error) {
        return ResponseEntity.status(500).body(new ErrorResponseDto().reason(error.getMessage()).reference(UUID.randomUUID()));
    }
}
