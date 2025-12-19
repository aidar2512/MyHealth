package kg.myhealth.api.controller;

import kg.myhealth.api.dto.notif.NotificationDto;
import kg.myhealth.api.mapper.NotificationMapper;
import kg.myhealth.domain.User;
import kg.myhealth.service.NotificationService;
import kg.myhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @GetMapping
    public List<NotificationDto> list(Authentication auth) {
        User u = userService.findByEmailOrThrow(auth.getName());
        return notificationService.list(u).stream().map(mapper::toDto).toList();
    }
}
