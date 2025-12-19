package kg.myhealth.api.dto.profile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import kg.myhealth.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileUpdateRequest {
    @Size(min = 2, max = 120)
    private String fullName;

    @Min(0) @Max(130)
    private Integer age;

    private Gender gender;
}
