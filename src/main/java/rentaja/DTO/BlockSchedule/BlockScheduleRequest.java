package rentaja.DTO.BlockSchedule;

import java.time.LocalDateTime;
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
    private LocalDateTime time;

    @NotBlank(message = "Reason cannot be empty")
    private String reason;

}
