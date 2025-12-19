package kg.myhealth.api.dto.health;

import jakarta.validation.constraints.*;
import kg.myhealth.domain.HealthMetricType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class CreateEntryRequest {
    @NotNull
    private HealthMetricType type;

    @NotNull @DecimalMin("0.0") @DecimalMax("300.0")
    private Double value;

    @NotBlank
    private String source; // manual/device

    private Instant recordedAt; // optional, default now
}
