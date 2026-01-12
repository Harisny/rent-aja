package rentaja.DTO.Booking;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    @NotNull(message = "Start time cannot be empty")
    @FutureOrPresent(message = "Start time must be now or in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be empty")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Field cannot be empty")
    private Integer fieldId;

}
