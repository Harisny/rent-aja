package rentaja.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rentaja.Entity.Booking;
import rentaja.Entity.Field;
import rentaja.Entity.Enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
        @Query(value = """
                        SELECT b FROM Booking b
                        JOIN b.user u
                        WHERE b.id = :id
                        AND u.email = :email
                        """)
        Optional<Booking> findByIdAndEmail(Integer id, String email);

        @Query(value = """
                        SELECT b FROM Booking b
                        JOIN b.user u
                        WHERE b.id = :id
                        AND u.email = :email
                        """)
        Optional<Booking> findByIdAndUserEmail(
                        @Param("id") Integer id,
                        @Param("email") String email);

        // Optional<Booking> findByIdAndUser_Email(Integer id, String email);

        boolean existsByFieldAndStartTimeLessThanAndEndTimeGreaterThan(Field field, LocalDateTime end,
                        LocalDateTime start);

        // JPQL
        @Query("""
                            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                            FROM Booking b
                            WHERE b.field.id = :fieldId
                                AND b.startTime < :endTime
                                AND b.endTime > :startTime
                                AND b.status IN :statuses
                        """)
        boolean checkSlot(
                        @Param("fieldId") Integer fieldId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime,
                        @Param("statuses") List<BookingStatus> statuses);

        @Query("""
                            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
                            FROM Booking b
                            WHERE b.field.id = :fieldId
                                AND b.id <> :id
                                AND b.startTime < :endTime
                                AND b.endTime > :startTime
                                AND b.status IN :statuses
                        """)
        boolean checkSlotUpdate(
                        @Param("fieldId") Integer fieldId,
                        @Param("id") Integer id,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime,
                        @Param("statuses") List<BookingStatus> statuses);
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
