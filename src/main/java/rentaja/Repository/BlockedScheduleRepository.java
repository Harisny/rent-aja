package rentaja.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rentaja.Entity.BlockedSchedule;
import rentaja.Entity.Field;

@Repository
public interface BlockedScheduleRepository extends JpaRepository<BlockedSchedule, Integer> {
    boolean existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(Field field, LocalDateTime end, LocalDateTime start);
}
