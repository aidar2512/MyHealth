package kg.myhealth.service;

import kg.myhealth.domain.Notification;
import kg.myhealth.domain.User;
import kg.myhealth.repo.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public Notification create(User user, String title, String message) {
        Notification n = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .read(false)
                .createdAt(Instant.now())
                .build();
        return repo.save(n);
    }

    public List<Notification> list(User user) {
        return repo.findTop100ByUserOrderByCreatedAtDesc(user);
    }
}
