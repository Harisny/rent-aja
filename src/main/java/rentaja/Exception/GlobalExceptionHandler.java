package rentaja.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rentaja.Exception.Exceptions.BadRequestException;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.InternalServerErrorException;
import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Exception.Exceptions.UnauthorizedException;
import rentaja.Utils.Response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
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

        // handle formated error atau validation
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors().forEach(err -> {
                        errors.put(err.getField(), err.getDefaultMessage());
                });

                ApiResponse<Object> res = ApiResponse.builder()
                                .message("Validation Failed")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .data(errors)
                                .build();
                return ResponseEntity.badRequest().body(res);
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        @ExceptionHandler(InternalServerErrorException.class)
        public ResponseEntity<ApiResponse<Object>> handleInternal(InternalServerErrorException ex) {

                ApiResponse<Object> res = ApiResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
}
