package rentaja.Service;

import java.util.List;

import org.hibernate.boot.beanvalidation.IntegrationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.transaction.Transactional;
import rentaja.DTO.BlockSchedule.BlockScheduleRequest;
import rentaja.DTO.BlockSchedule.BlockScheduleResponse;
import rentaja.Entity.BlockedSchedule;
import rentaja.Entity.Field;
import rentaja.Entity.Enums.FieldStatus;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Repository.BlockedScheduleRepository;
import rentaja.Repository.BookingRepository;
import rentaja.Repository.FieldRepository;

@Service
public class BlockedScheduleService {
    private final BlockedScheduleRepository repo;
    private final FieldRepository fieldRepo;
    private final BookingRepository bookRepo;

    public BlockedScheduleService(BlockedScheduleRepository repo, FieldRepository fieldRepo,
            BookingRepository bookRepo) {
        this.repo = repo;
        this.fieldRepo = fieldRepo;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public BlockScheduleResponse blockSlot(BlockScheduleRequest req) {
        Field field = fieldRepo.findById(req.getFieldId())
                .orElseThrow(() -> new NotFoundException("field id : " + req.getFieldId() + " didnt exist"));

        if (bookRepo.existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(field, req.getEndTime(),
                req.getStartTime())) {
            throw new ConflictException("field is already booking by user");
        }

        Field fieldStatus = new Field();
        fieldStatus.setStatus(FieldStatus.UNAVAILABLE);

        BlockedSchedule data = new BlockedSchedule();
        data.setStartTime(req.getStartTime());
        data.setEndTime(req.getEndTime());
        data.setReason(req.getReason());
        data.setField(field);

        BlockedSchedule saved = repo.save(data);
        return new BlockScheduleResponse(saved);
    }

    public List<BlockScheduleResponse> blockSchedules() {
        List<BlockedSchedule> data;
        try {
            data = repo.findAll();
        } catch (InternalServerError e) {
            throw new IntegrationException("cannot retrieve blocked schedule");
        }
        return data.stream().map(BlockScheduleResponse::new).toList();
    }

    public void remove(Integer id) {
        try {
            repo.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("block schedule id : " + id + " was not found");
        }
    }
}
