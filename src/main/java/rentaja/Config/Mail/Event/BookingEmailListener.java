package rentaja.Config.Mail.Event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import rentaja.Entity.Booking;
import rentaja.Service.EmailService;

@Component
@Slf4j
public class BookingEmailListener {
    private final EmailService emailService;

    public BookingEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        Booking booking = event.getBooking();
        emailService.sendBookingSuccessEmail(booking.getUser(), booking);
    }
}
