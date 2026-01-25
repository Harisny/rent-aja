package rentaja.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import rentaja.DTO.Field.FieldRequest;
import rentaja.DTO.Field.FieldResponse;
import rentaja.Entity.Field;
import rentaja.Entity.Enums.FieldStatus;
import rentaja.Exception.Exceptions.ConflictException;
import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Repository.FieldRepository;

@Service
public class FieldService {
    private final FieldRepository repo;

    public FieldService(FieldRepository repo) {
        this.repo = repo;
    }

    public FieldResponse create(FieldRequest req) {

        if (repo.findByName(req.getName()).isPresent()) {
            throw new ConflictException("field name is already exist");
        }

        Field field = new Field();
        field.setName(req.getName());
        field.setOpenTime(req.getOpenTime());
        field.setCloseTime(req.getCloseTime());
        field.setStatus(FieldStatus.AVAILABLE);

        Field saved = repo.save(field);

        return new FieldResponse(saved);
    }

    public List<FieldResponse> fields() {
        List<Field> data = repo.findAll();
        return data.stream().map(FieldResponse::new).toList();
    }

    public void delete(Integer id) {
        Field field = repo.findById(id).orElseThrow(() -> new NotFoundException("field id : " + id + " didnt Exist"));

        repo.deleteById(field.getId());
    }
}
