package rentaja.Service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import rentaja.Config.Security.JwtService;
import rentaja.DTO.Auth.AuthRequest;
import rentaja.DTO.Auth.AuthResponse;
import rentaja.Entity.User;
import rentaja.Entity.Enums.Role;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.InternalServerErrorException;
import rentaja.Exception.Exceptions.UnauthorizedException;
import rentaja.Repository.UserRepository;

@Service
@Slf4j
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authentication;
    private final JwtService jwtService;

    public AuthService(UserRepository repo, PasswordEncoder encoder, AuthenticationManager auth, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.authentication = auth;
        this.jwtService = jwt;
    }

    @Transactional
    public void register(AuthRequest req) {
        // tidak efektif karena terjadi Race condition (2 request bersamaan)

        // if (repo.findByEmail(req.getEmail()).isPresent()) {
        // throw new ConflictException("Email Registered");
        // }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.ROLE_USER);

        try {
            repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("email registered");
        } catch (Exception e) {
            log.error("register failed", e);
            throw new InternalServerErrorException("failed to proccess user operation");
        }
    }

    public AuthResponse login(AuthRequest req) {
        try {
            authentication.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("wrong password or email");
        }

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new InternalServerErrorException("user authenticated but not found"));

        String token;
        try {
            token = jwtService.generateToken(user);
        } catch (Exception e) {
            log.error("jwt generation failed", e);
            throw new InternalServerErrorException("login failed");
        }
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }
}
