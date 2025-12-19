package kg.myhealth.service;

import kg.myhealth.domain.Gender;
import kg.myhealth.domain.User;
import kg.myhealth.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    private String normEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is required");
        return email.trim().toLowerCase();
    }

    public User register(String email, String rawPassword, String fullName, Integer age, Gender gender) {
        String e = normEmail(email);
        if (userRepo.existsByEmail(e)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Password is required");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Full name is required");

        User user = User.builder()
                .email(e)
                .passwordHash(encoder.encode(rawPassword))
                .fullName(fullName.trim())
                .age(age)
                .gender(gender)
                .createdAt(Instant.now())
                .build();
        return userRepo.save(user);
    }

    public User findByEmailOrThrow(String email) {
        String e = normEmail(email);
        return userRepo.findByEmail(e)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User updateProfile(String email, String fullName, Integer age, Gender gender) {
        User user = findByEmailOrThrow(email);
        if (fullName != null && !fullName.isBlank()) user.setFullName(fullName.trim());
        if (age != null) user.setAge(age);
        if (gender != null) user.setGender(gender);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmailOrThrow(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("USER")
                .build();
    }

    public boolean passwordMatches(User user, String raw) {
        return encoder.matches(raw, user.getPasswordHash());
    }
}
