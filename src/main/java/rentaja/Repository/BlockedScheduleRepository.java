package rentaja.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rentaja.Entity.BlockedSchedule;

@Repository
public interface BlockedScheduleRepository extends JpaRepository<BlockedSchedule, Integer> {
        @Query("""
                        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                        FROM BlockedSchedule b
                        WHERE b.field.id = :fieldId
                                AND b.time = :time
                        """)
        boolean checkSlot(
                        @Param("fieldId") Integer fieldId,
                        @Param("time") LocalDateTime time);
}
