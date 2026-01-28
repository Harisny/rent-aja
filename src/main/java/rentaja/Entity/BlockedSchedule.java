package rentaja.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "blocked_schedules")
@Getter
@Setter
public class BlockedSchedule {
    @Id
    @SequenceGenerator(name = "blocked_schedule_seq", sequenceName = "blocked_schedule_seq", allocationSize = 1)
    @GeneratedValue(generator = "blocked_schedule_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;
}
