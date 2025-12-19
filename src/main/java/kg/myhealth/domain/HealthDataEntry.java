package kg.myhealth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "health_data_entries")
public class HealthDataEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthMetricType type;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String source; // manual/device

    @Column(nullable = false)
    private Instant recordedAt;
}
