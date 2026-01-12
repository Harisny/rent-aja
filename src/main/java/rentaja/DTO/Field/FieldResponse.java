package rentaja.DTO.Field;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import rentaja.Entity.Field;

@Getter
@Setter
public class FieldResponse {
    private Integer id;
    private String name;
    private LocalTime openTime;
    private LocalTime closeTime;

    public FieldResponse(Field field) {
        this.id = field.getId();
        this.name = field.getName();
        this.openTime = field.getOpenTime();
        this.closeTime = field.getCloseTime();
    }
}
