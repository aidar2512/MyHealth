package kg.myhealth.api.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        ApiError body = new ApiError(
                Instant.now(), 400, "Bad Request", "Validation error",
                req.getRequestURI(), fields
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCreds(BadCredentialsException ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(), 401, "Unauthorized", ex.getMessage(),
                req.getRequestURI(), null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({UsernameNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleNotFoundOrBadRequest(Exception ex, HttpServletRequest req) {
        HttpStatus st = (ex instanceof UsernameNotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError(
                Instant.now(), st.value(), st.getReasonPhrase(), ex.getMessage(),
                req.getRequestURI(), null
        );
        return ResponseEntity.status(st).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(), 500, "Internal Server Error",
                "Server error: " + ex.getClass().getSimpleName(),
                req.getRequestURI(), null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
