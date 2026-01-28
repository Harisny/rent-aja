package rentaja.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import rentaja.Entity.Booking;
import rentaja.Entity.User;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendBookingSuccessEmail(User user, Booking booking) {
        try {
            Context context = new Context();
            context.setVariable("name", user.getEmail());
            context.setVariable("field", booking.getField().getName());
            context.setVariable("time", booking.getTime());

            String html = templateEngine.process("booking-success", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Booking Confirmation");
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send booking email", e);
        }
    }
}
