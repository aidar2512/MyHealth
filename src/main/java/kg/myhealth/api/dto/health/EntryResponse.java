package kg.myhealth.api.dto.health;

import kg.myhealth.domain.HealthMetricType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter @AllArgsConstructor
public class EntryResponse {
    private Long id;
    private HealthMetricType type;
    private Double value;
    private String source;
    private Instant recordedAt;
}
