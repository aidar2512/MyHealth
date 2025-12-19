package kg.myhealth.service;

import kg.myhealth.domain.Gender;
import kg.myhealth.domain.User;
import kg.myhealth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public String register(String email, String rawPassword, String fullName, Integer age, Gender gender) {
        User user = userService.register(email, rawPassword, fullName, age, gender);
        return jwtService.generateToken(user.getEmail());
    }

    public String login(String email, String rawPassword) {
        User user = userService.findByEmailOrThrow(email);
        if (!userService.passwordMatches(user, rawPassword)) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return jwtService.generateToken(user.getEmail());
    }
}
