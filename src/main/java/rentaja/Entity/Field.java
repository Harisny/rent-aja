package rentaja.Entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rentaja.Entity.Enums.FieldStatus;

@Entity
@Table(name = "fields", uniqueConstraints = @UniqueConstraint(columnNames = "field_name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @SequenceGenerator(name = "field_seq", sequenceName = "field_seq", allocationSize = 1)
    @GeneratedValue(generator = "field_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "field_name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldStatus status;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime closeTime;
}
