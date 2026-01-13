package rentaja.DTO.BlockSchedule;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import rentaja.Entity.BlockedSchedule;

@Getter
@Setter
public class BlockScheduleResponse {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private Integer fieldId;

    public BlockScheduleResponse(BlockedSchedule bc) {
        this.id = bc.getId();
        this.startTime = bc.getStartTime();
        this.endTime = bc.getEndTime();
        this.reason = bc.getReason();
        this.fieldId = bc.getField().getId();
    }
}
