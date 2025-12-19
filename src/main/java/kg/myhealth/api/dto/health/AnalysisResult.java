package kg.myhealth.api.dto.health;

import kg.myhealth.domain.HealthMetricType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AnalysisResult {
    private HealthMetricType type;
    private Double value;
    private String status; // LOW/NORMAL/HIGH
    private String message;
}
