package kg.myhealth.api.mapper;

import kg.myhealth.api.dto.notif.NotificationDto;
import kg.myhealth.domain.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification n) {
        return new NotificationDto(n.getId(), n.getTitle(), n.getMessage(), n.isRead(), n.getCreatedAt());
    }
}
