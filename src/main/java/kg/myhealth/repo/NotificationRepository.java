package kg.myhealth.repo;

import kg.myhealth.domain.Notification;
import kg.myhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop100ByUserOrderByCreatedAtDesc(User user);
}
