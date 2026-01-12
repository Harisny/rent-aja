package rentaja.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rentaja.Entity.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
}
