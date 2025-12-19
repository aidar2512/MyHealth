package kg.myhealth.api.controller;

import kg.myhealth.api.dto.rec.RecommendationDto;
import kg.myhealth.domain.User;
import kg.myhealth.service.RecommendationService;
import kg.myhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping("/latest")
    public RecommendationDto latest(Authentication auth) {
        User u = userService.findByEmailOrThrow(auth.getName());
        return recommendationService.latest(u);
    }
}
