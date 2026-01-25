package rentaja.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
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
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                if (!email.equals(auth.getName())) {
                        throw new AccessDeniedException("Forbidden");
                }

                User user = userRepo.findByEmail(auth.getName())
                                .orElseThrow(() -> new NotFoundException(
                                                "user : " + auth.getName() + " was not found"));

                Field field = fieldRepo.findById(req.getFieldId())
                                .orElseThrow(() -> new NotFoundException(
                                                "field id : " + user.getId() + " was not found"));

                if (repo.checkSlot(field.getId(), req.getStartTime(), req.getEndTime(),
                                List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING_PAYMENT))
                                || blockRepo.checkSlot(field.getId(), req.getStartTime(), req.getEndTime())) {
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

        public BookingResponse bookDetail(Integer id) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                Booking booking = repo.findById(id)
                                .orElseThrow(() -> new NotFoundException("Booking id " + id + " tidak ditemukan"));

                if (!booking.getUser().getEmail().equals(auth.getName())) {
                        throw new AccessDeniedException("Forbidden");
                }

                return new BookingResponse(booking);
        }

        // @PreAuthorize("#booking.user.email == authentication.name") bisa seperti ini,
        // clean code
        @Transactional
        public BookingResponse bookModify(Integer id, BookingRequest req) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Booking booking = repo.findByIdAndEmail(id, auth.getName())
                                .orElseThrow(() -> new AccessDeniedException("Forbidden"));

                if (booking.getStatus() == BookingStatus.CANCELLED) {
                        throw new ConflictException("cancelled booking cannot be updated");
                }

                // field Id dari booking
                Field field = booking.getField();
                if (!field.getId().equals(req.getFieldId())) {
                        // jika field lama ingin di update field baru
                        field = fieldRepo.findById(req.getFieldId())
                                        .orElseThrow(() -> new NotFoundException("field id : " + " was not found"));
                }

                // checking jika slot tersedia
                if (repo.checkSlotUpdate(field.getId(), id, req.getStartTime(), req.getEndTime(),
                                List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING_PAYMENT))) {
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

        public void bookRemove(Integer id) {
                Booking booking = repo.findById(id)
                                .orElseThrow(() -> new NotFoundException("field id : " + id + " was not found"));

                repo.delete(booking);
        }
}
