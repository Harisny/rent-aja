package rentaja.DTO.Auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
    private Integer id;
    private String email;
    private String role;
    private String token;
}
