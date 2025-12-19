package kg.myhealth.api.dto.profile;

import kg.myhealth.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ProfileResponse {
    private String email;
    private String fullName;
    private Integer age;
    private Gender gender;
}
