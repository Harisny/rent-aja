package rentaja.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rentaja.DTO.Booking.BookingRequest;
import rentaja.DTO.Booking.BookingResponse;
import rentaja.Service.BookingService;
import rentaja.Utils.Response.ApiResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/booking")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookingResponse data = service.bookSlot(req, userDetails.getUsername());
        ApiResponse<BookingResponse> res = ApiResponse.<BookingResponse>builder()
                .message("create booking is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> bookings() {
        List<BookingResponse> data = service.bookings();
        ApiResponse<List<BookingResponse>> res = ApiResponse.<List<BookingResponse>>builder()
                .message("retrieve bookings is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/booking/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<BookingResponse>> booking(@PathVariable("id") Integer id) {
        BookingResponse data = service.bookDetail(id);
        ApiResponse<BookingResponse> res = ApiResponse.<BookingResponse>builder()
                .message("retrieve booking is Success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/booking/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<BookingResponse>> reschedule(
            @Valid @PathVariable("id") Integer id, @RequestBody BookingRequest req) {

        BookingResponse data = service.bookModify(id, req);
        ApiResponse<BookingResponse> res = ApiResponse.<BookingResponse>builder()
                .message("modify/update booking is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/booking/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> remove(@PathVariable("id") Integer id) {
        service.bookRemove(id);
        ApiResponse<BookingResponse> res = ApiResponse.<BookingResponse>builder()
                .message("remove Booking is Success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(res);
    }
}
