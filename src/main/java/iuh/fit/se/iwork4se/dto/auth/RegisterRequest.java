
package iuh.fit.se.iwork4se.dto.auth;

import iuh.fit.se.iwork4se.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @Email String email,        // có thể null nếu đăng ký bằng phone
        String phone,        // có thể null nếu đăng ký bằng email
        @NotBlank(message = "Password is required") String password,     // bắt buộc
        @NotBlank(message = "Full name is required") String fullName,     // bắt buộc
        @NotNull(message = "Role is required") Role role            // cho phép JOBSEEKER | EMPLOYER; null => JOBSEEKER
) {}
