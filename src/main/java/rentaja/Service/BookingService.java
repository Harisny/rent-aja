package rentaja.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import rentaja.Config.Mail.Event.BookingCreatedEvent;
import rentaja.DTO.Booking.BookingRequest;
import rentaja.DTO.Booking.BookingResponse;
import rentaja.Entity.Booking;
import rentaja.Entity.Field;
import rentaja.Entity.User;
import rentaja.Entity.Enums.BookingStatus;
import rentaja.Entity.Enums.FieldStatus;
import rentaja.Exception.Exceptions.BadRequestException;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Exception.Exceptions.UnauthorizedException;
import rentaja.Repository.BlockedScheduleRepository;
import rentaja.Repository.BookingRepository;
import rentaja.Repository.FieldRepository;
import rentaja.Repository.UserRepository;

@Service
@Slf4j
public class BookingService {
        private final BookingRepository repo;
        private final FieldRepository fieldRepo;
        private final UserRepository userRepo;
        private final BlockedScheduleRepository blockRepo;
        private final ApplicationEventPublisher eventPublisher;

        public BookingService(BookingRepository repo, FieldRepository fieldRepo, UserRepository userRepo,
                        BlockedScheduleRepository blockRepo, ApplicationEventPublisher eventPublisher) {
                this.repo = repo;
                this.fieldRepo = fieldRepo;
                this.userRepo = userRepo;
                this.blockRepo = blockRepo;
                this.eventPublisher = eventPublisher;
        }

        @Transactional
        public BookingResponse bookSlot(BookingRequest req, String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("user : " + email + " was not found"));

                Field field = fieldRepo.findById(req.getFieldId())
                                .orElseThrow(() -> new NotFoundException(
                                                "field id : " + user.getId() + " was not found"));

                if (repo.existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(field, req.getEndTime(),
                                req.getStartTime())
                                || blockRepo.existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(field,
                                                req.getEndTime(),
                                                req.getStartTime())) {
                        throw new ConflictException("slot is not available");
                }

                if (field.getStatus().equals(FieldStatus.UNAVAILABLE)) {
                        throw new BadRequestException("field is not available");
                }

                Booking booking = new Booking();
                booking.setUser(user);
                booking.setField(field);
                booking.setStartTime(req.getStartTime());
                booking.setEndTime(req.getEndTime());
                booking.setStatus(BookingStatus.CONFIRMED);
                booking.setCreatedAt(LocalDateTime.now());

                Booking saved = repo.save(booking);
                eventPublisher.publishEvent(new BookingCreatedEvent(saved));
                return new BookingResponse(saved);
        }

        public List<BookingResponse> bookings() {
                List<Booking> booking = repo.findAll();
                return booking.stream().map(BookingResponse::new).toList();
        }

        public BookingResponse detail(Integer id) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Booking booking = repo.findById(id).orElseThrow(
                                () -> new UnauthorizedException("Booking id : " + id + " was not found"));

                if (booking.getUser().getEmail().equals(auth.getName())) {
                        throw new UnauthorizedException("You are not allowed to access this booking");
                }
                return new BookingResponse(booking);
        }

        @Transactional
        public BookingResponse modify(Integer id, BookingRequest req) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Booking booking = repo.findById(id)
                                .orElseThrow(() -> new NotFoundException("Booking id : " + id + " was not found"));

                if (!booking.getUser().getEmail().equals(auth.getName())) {
                        throw new UnauthorizedException("You are not allowed to access this booking");
                }

                if (booking.getStatus() == BookingStatus.CANCELLED) {
                        throw new ConflictException("cancelled booking cannot be updated");
                }

                // field Id from booking
                Field field = booking.getField();
                if (!field.getId().equals(req.getFieldId())) {
                        // jika field lama ingin di update field baru
                        field = fieldRepo.findById(req.getFieldId())
                                        .orElseThrow(() -> new NotFoundException("field id : " + " was not found"));
                }

                // checking jika slot tersedia
                if (repo.checkSlot(field.getId(), id, req.getStartTime(), req.getEndTime())) {
                        throw new ConflictException("slot is not available");
                }

                booking.setField(field);
                booking.setStartTime(req.getStartTime());
                booking.setEndTime(req.getEndTime());
                booking.setStatus(BookingStatus.RESCHEDULED);
                booking.setUpdateAt(LocalDateTime.now());

                Booking saved = repo.save(booking);
                eventPublisher.publishEvent(new BookingCreatedEvent(booking));
                return new BookingResponse(saved);
        }

        public void remove(Integer id) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Booking booking = repo.findById(id)
                                .orElseThrow(() -> new NotFoundException("Booking id : " + id + " was not found"));

                if (!booking.getUser().getEmail().equals(auth.getName())) {
                        throw new UnauthorizedException("You are not allowed to access this booking");
                }

                repo.delete(booking);
        }
}
