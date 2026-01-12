package rentaja.Service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import rentaja.DTO.Field.FieldRequest;
import rentaja.DTO.Field.FieldResponse;
import rentaja.Entity.Field;
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
        Field field = new Field();
        field.setName(req.getName());
        field.setOpenTime(req.getOpenTime());
        field.setCloseTime(req.getCloseTime());

        Field saved;

        try {
            saved = repo.save(field);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("field name is already exist");
        }
        return new FieldResponse(saved);
    }

    public List<FieldResponse> fields() {
        List<Field> data = repo.findAll();
        return data.stream().map(FieldResponse::new).toList();
    }

    public void delete(Integer id) {
        try {
            repo.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("field id : " + id + " didnt Exist");
        }
    }
}
