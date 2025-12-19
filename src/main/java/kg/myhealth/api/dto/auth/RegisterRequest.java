package kg.myhealth.api.dto.auth;

import jakarta.validation.constraints.*;
import kg.myhealth.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    @NotBlank @Size(min = 2, max = 120)
    private String fullName;

    @Min(0) @Max(130)
    private Integer age;

    private Gender gender;
}
