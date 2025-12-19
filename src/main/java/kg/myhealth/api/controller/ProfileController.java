package kg.myhealth.api.controller;

import jakarta.validation.Valid;
import kg.myhealth.api.dto.profile.ProfileResponse;
import kg.myhealth.api.dto.profile.ProfileUpdateRequest;
import kg.myhealth.api.mapper.UserMapper;
import kg.myhealth.domain.User;
import kg.myhealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ProfileResponse me(Authentication auth) {
        User u = userService.findByEmailOrThrow(auth.getName());
        return userMapper.toProfileResponse(u);
    }

    @PutMapping
    public ProfileResponse update(Authentication auth, @Valid @RequestBody ProfileUpdateRequest req) {
        User u = userService.updateProfile(auth.getName(), req.getFullName(), req.getAge(), req.getGender());
        return userMapper.toProfileResponse(u);
    }
}
