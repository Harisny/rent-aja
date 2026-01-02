package rentaja.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rentaja.DTO.Auth.AuthRequest;
import rentaja.DTO.Auth.AuthResponse;
import rentaja.Service.AuthService;
import rentaja.Utils.Response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody AuthRequest req) {
        service.register(req);
        ApiResponse<String> res = ApiResponse.<String>builder()
                .message("Register is Successfully")
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(res);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        AuthResponse data = service.login(req);
        ApiResponse<AuthResponse> res = ApiResponse.<AuthResponse>builder()
                .message("Login is Successfully")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();

        return ResponseEntity.ok(res);
    }

}
