package rentaja.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be format @")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
