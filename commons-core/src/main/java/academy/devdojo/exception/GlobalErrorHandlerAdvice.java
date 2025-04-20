package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class    GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handlerNotFoundException(NotFoundException e) {
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
