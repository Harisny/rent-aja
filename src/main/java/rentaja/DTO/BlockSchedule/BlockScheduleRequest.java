package rentaja.DTO.BlockSchedule;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockScheduleRequest {
    @NotNull(message = "Field cannot be empty")
    private Integer fieldId;

    @NotNull(message = "Start time cannot be empty")
    @FutureOrPresent(message = "Start time must be now or in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be empty")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotBlank(message = "Reason cannot be empty")
    private String reason;

}
