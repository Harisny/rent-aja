package rentaja.Config.Mail.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rentaja.Entity.Booking;

@Getter
@AllArgsConstructor
public class BookingCreatedEvent {
    private Booking booking;
}
