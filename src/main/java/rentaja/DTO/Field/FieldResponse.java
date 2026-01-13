package rentaja.DTO.Field;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import rentaja.Entity.Field;
import rentaja.Entity.Enums.FieldStatus;

@Getter
@Setter
public class FieldResponse {
    private Integer id;
    private String name;
    private LocalTime openTime;
    private LocalTime closeTime;
    private FieldStatus status;

    public FieldResponse(Field field) {
        this.id = field.getId();
        this.name = field.getName();
        this.openTime = field.getOpenTime();
        this.closeTime = field.getCloseTime();
        this.status = field.getStatus();
    }
}
