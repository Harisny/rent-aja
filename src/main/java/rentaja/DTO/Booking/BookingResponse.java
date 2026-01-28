package rentaja.DTO.Booking;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import rentaja.Entity.Booking;
import rentaja.Entity.Enums.BookingStatus;

@Getter
@Setter
public class BookingResponse {
    private Integer id;
    private String user;
    private String field;
    private LocalDateTime time;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateTime;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.user = booking.getUser().getUsername();
        this.field = booking.getField().getName();
        this.time = booking.getTime();
        this.status = booking.getStatus();
        this.createdAt = booking.getCreatedAt();
        this.updateTime = booking.getUpdateAt();
    }
}
