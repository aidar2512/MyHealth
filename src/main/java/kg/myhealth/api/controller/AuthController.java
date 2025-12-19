package kg.myhealth.api.controller;

import jakarta.validation.Valid;
import kg.myhealth.api.dto.auth.AuthResponse;
import kg.myhealth.api.dto.auth.LoginRequest;
import kg.myhealth.api.dto.auth.RegisterRequest;
import kg.myhealth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        String token = authService.register(
                req.getEmail(), req.getPassword(), req.getFullName(), req.getAge(), req.getGender()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
