package nl.kooi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleValidationException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
