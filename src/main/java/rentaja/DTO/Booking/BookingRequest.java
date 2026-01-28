package rentaja.DTO.Booking;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    @NotNull(message = "Time cannot be empty")
    @FutureOrPresent(message = "Time must be now or in the future")
    private LocalDateTime time;

    @NotNull(message = "Field cannot be empty")
    private Integer fieldId;
}
