package rentaja.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import rentaja.Exception.Exceptions.BadRequestException;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Utils.Response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        // handle business flow
        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.NOT_FOUND.value())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ApiResponse<Object>> handleConflict(ConflictException ex) {
                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.CONFLICT.value())
                                .build();

                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {

                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        // handle conflict data dari race condition
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
                log.warn("Race condition / DB constraint violation", ex);

                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.CONFLICT.value())
                                .build();

                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        // handle formated error atau validation
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors().forEach(err -> {
                        errors.put(err.getField(), err.getDefaultMessage());
                });

                ApiResponse<Map<String, String>> res = ApiResponse.<Map<String, String>>builder()
                                .message("Validation Failed")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .errors(errors)
                                .build();
                return ResponseEntity.badRequest().body(res);
        }

        // handle authentication
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiResponse<Object>> handleAuth(AuthenticationException ex) {
                log.error("authentication error", ex);

                ApiResponse<Object> res = ApiResponse.builder()
                                .status(401)
                                .message(ex.getMessage())
                                .build();
                // .message("wrong email and password") go dan cek perbedaannya
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
                log.error("access denied error", ex);

                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.FORBIDDEN.value())
                                .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }

        // db error
        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ApiResponse<Object>> handleDb(DataAccessException ex) {
                log.error("Database error", ex);

                ApiResponse<Object> res = ApiResponse.builder()
                                .status(500)
                                .message("database unavailable")
                                .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
}
