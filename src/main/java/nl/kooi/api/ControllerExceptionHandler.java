package nl.kooi.api;

import nl.kooi.api.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponseDto> handleValidationException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto().reason(exception.getMessage()).reference(UUID.randomUUID()));
    }
}
