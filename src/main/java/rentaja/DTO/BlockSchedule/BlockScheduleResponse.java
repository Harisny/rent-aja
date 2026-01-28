package rentaja.DTO.BlockSchedule;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import rentaja.Entity.BlockedSchedule;

@Getter
@Setter
public class BlockScheduleResponse {
    private Integer id;
    private LocalDateTime time;
    private String reason;
    private Integer fieldId;

    public BlockScheduleResponse(BlockedSchedule bc) {
        this.id = bc.getId();
        this.time = bc.getTime();
        this.reason = bc.getReason();
        this.fieldId = bc.getField().getId();
    }
}
