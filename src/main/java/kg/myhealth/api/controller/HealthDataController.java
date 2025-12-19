package kg.myhealth.api.controller;

import jakarta.validation.Valid;
import kg.myhealth.api.dto.health.CreateEntryRequest;
import kg.myhealth.api.dto.health.EntryResponse;
import kg.myhealth.api.mapper.HealthEntryMapper;
import kg.myhealth.domain.User;
import kg.myhealth.service.AnalysisService;
import kg.myhealth.service.HealthDataService;
import kg.myhealth.service.NotificationService;
import kg.myhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-data")
@RequiredArgsConstructor
public class HealthDataController {

    private final UserService userService;
    private final HealthDataService healthDataService;
    private final AnalysisService analysisService;
    private final NotificationService notificationService;
    private final HealthEntryMapper mapper;

    @PostMapping
    public EntryResponse add(Authentication auth, @Valid @RequestBody CreateEntryRequest req) {
        User u = userService.findByEmailOrThrow(auth.getName());

        var entry = healthDataService.addEntry(u, req.getType(), req.getValue(), req.getSource(), req.getRecordedAt());

        var analysis = analysisService.analyze(req.getType(), req.getValue());
        if (!"NORMAL".equals(analysis.getStatus())) {
            notificationService.create(u,
                    "Health Alert: " + req.getType().name(),
                    analysis.getMessage() + " Value=" + req.getValue());
        }

        return mapper.toResponse(entry);
    }

    @GetMapping("/latest")
    public List<EntryResponse> latest(Authentication auth) {
        User u = userService.findByEmailOrThrow(auth.getName());
        return healthDataService.latest(u).stream().map(mapper::toResponse).toList();
    }
}
