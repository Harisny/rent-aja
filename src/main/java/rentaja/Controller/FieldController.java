package rentaja.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rentaja.DTO.Field.FieldRequest;
import rentaja.DTO.Field.FieldResponse;
import rentaja.Service.FieldService;
import rentaja.Utils.Response.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class FieldController {
    private FieldService service;

    public FieldController(FieldService service) {
        this.service = service;
    }

    @PostMapping("/field")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FieldResponse>> field(@Valid @RequestBody FieldRequest req) {
        FieldResponse data = service.create(req);
        ApiResponse<FieldResponse> res = ApiResponse.<FieldResponse>builder()
                .message("create field is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/fields")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<FieldResponse>>> fields() {
        List<FieldResponse> data = service.fields();
        ApiResponse<List<FieldResponse>> res = ApiResponse.<List<FieldResponse>>builder()
                .message("create field is success")
                .status(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/field/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FieldResponse>> remove(@PathVariable("id") Integer id) {
        service.delete(id);
        ApiResponse<FieldResponse> res = ApiResponse.<FieldResponse>builder()
                .message("remove field is success")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(res);
    }
}
