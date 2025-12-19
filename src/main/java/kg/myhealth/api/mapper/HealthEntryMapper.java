package kg.myhealth.api.mapper;

import kg.myhealth.api.dto.health.EntryResponse;
import kg.myhealth.domain.HealthDataEntry;
import org.springframework.stereotype.Component;

@Component
public class HealthEntryMapper {
    public EntryResponse toResponse(HealthDataEntry e) {
        return new EntryResponse(e.getId(), e.getType(), e.getValue(), e.getSource(), e.getRecordedAt());
    }
}
