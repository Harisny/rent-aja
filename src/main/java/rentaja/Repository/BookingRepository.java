package rentaja.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rentaja.Entity.Booking;
import rentaja.Entity.Field;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
        boolean existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(Field field, LocalDateTime end,
                        LocalDateTime start);

        // JPQL
        @Query(value = """
                        SELECT COUNT(b) > 0 FROM Booking b
                        WHERE b.field.id = :fieldId
                        AND b.id <> :id
                        AND b.startTime < :endTime
                        AND b.endTime > :startTime
                                """)
        boolean checkSlot(
                        Integer fieldId,
                        Integer id,
                        LocalDateTime startTime,
                        LocalDateTime endTime);
}

// NATIVE SQL

// @Query(value = """
// SELECT COUNT(*) > 0
// FROM bookings
// WHERE field_id = :fieldId
// AND id <> :id
// AND start_time < :endTime
// AND end_time > :startTime
// """, nativeQuery = true)
// boolean checkSlot(
// Integer fieldId,
// Integer id,
// LocalDateTime endTime,
// LocalDateTime startTime)
// ;
