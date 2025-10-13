package iuh.fit.se.iwork4se.dto;

import iuh.fit.se.iwork4se.model.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 150)
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    // optional: cho phép client chọn role khi đăng ký (hoặc cố định JOBSEEKER)
    private Role role = Role.JOBSEEKER;
}
