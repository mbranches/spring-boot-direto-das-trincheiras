package academy.devdojo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class    GlobalErrorHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handlerNotFoundException( NotFoundException e) {
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String defaultMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.joining(","));

        ApiError response = ApiError.builder()
                .timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI())
                .message(defaultMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
