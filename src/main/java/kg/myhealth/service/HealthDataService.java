package kg.myhealth.service;

import kg.myhealth.domain.*;
import kg.myhealth.repo.HealthDataRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HealthDataService {

    private final HealthDataRepository repo;

    public HealthDataService(HealthDataRepository repo) {
        this.repo = repo;
    }

    public HealthDataEntry addEntry(User user, HealthMetricType type, Double value, String source, Instant recordedAt) {
        if (value == null || value.isNaN() || value.isInfinite()) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        if (recordedAt == null) recordedAt = Instant.now();
        HealthDataEntry e = HealthDataEntry.builder()
                .user(user)
                .type(type)
                .value(value)
                .source(source)
                .recordedAt(recordedAt)
                .build();
        return repo.save(e);
    }

    public List<HealthDataEntry> latest(User user) {
        return repo.findTop200ByUserOrderByRecordedAtDesc(user);
    }
}
