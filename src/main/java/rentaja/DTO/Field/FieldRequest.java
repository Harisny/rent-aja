package rentaja.DTO.Field;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldRequest {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Open time cannot be emty")
    private LocalTime openTime;
    @NotNull(message = "Close time cannot be emty")
    private LocalTime closeTime;
}
