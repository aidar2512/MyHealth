package kg.myhealth.repo;

import kg.myhealth.domain.HealthDataEntry;
import kg.myhealth.domain.HealthMetricType;
import kg.myhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthDataRepository extends JpaRepository<HealthDataEntry, Long> {
    List<HealthDataEntry> findTop200ByUserOrderByRecordedAtDesc(User user);
    List<HealthDataEntry> findByUserAndTypeOrderByRecordedAtDesc(User user, HealthMetricType type);
}
