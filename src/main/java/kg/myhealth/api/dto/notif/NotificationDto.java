package kg.myhealth.api.dto.notif;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter @AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private boolean read;
    private Instant createdAt;
}
