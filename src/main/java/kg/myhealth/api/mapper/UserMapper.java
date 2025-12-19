package kg.myhealth.api.mapper;

import kg.myhealth.api.dto.profile.ProfileResponse;
import kg.myhealth.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public ProfileResponse toProfileResponse(User u) {
        return new ProfileResponse(u.getEmail(), u.getFullName(), u.getAge(), u.getGender());
    }
}
