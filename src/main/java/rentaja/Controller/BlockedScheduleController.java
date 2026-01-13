package rentaja.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rentaja.DTO.BlockSchedule.BlockScheduleRequest;
import rentaja.DTO.BlockSchedule.BlockScheduleResponse;
import rentaja.Service.BlockedScheduleService;
import rentaja.Utils.Response.ApiResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class BlockedScheduleController {
    private final BlockedScheduleService service;

    public BlockedScheduleController(BlockedScheduleService service) {
        this.service = service;
    }

    @PostMapping("/block-schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BlockScheduleResponse>> blockSlot(@RequestBody BlockScheduleRequest req) {
        BlockScheduleResponse data = service.blockSlot(req);
        ApiResponse<BlockScheduleResponse> res = ApiResponse.<BlockScheduleResponse>builder()
                .message("create blocking is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/blocked-schedules")
    public ResponseEntity<ApiResponse<List<BlockScheduleResponse>>> blockedSchedules() {
        List<BlockScheduleResponse> data = service.blockSchedules();
        ApiResponse<List<BlockScheduleResponse>> res = ApiResponse.<List<BlockScheduleResponse>>builder()
                .message("retrieve data blocking is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/block-schedule/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BlockScheduleResponse>> blockSlot(@PathVariable("id") Integer id) {
        service.remove(id);
        ApiResponse<BlockScheduleResponse> res = ApiResponse.<BlockScheduleResponse>builder()
                .message("remove data blocking id : " + id + " is success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(res);
    }
}
