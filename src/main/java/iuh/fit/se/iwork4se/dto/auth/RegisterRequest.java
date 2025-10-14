
package iuh.fit.se.iwork4se.dto.auth;

import iuh.fit.se.iwork4se.model.Role;

public record RegisterRequest(
        String email,        // có thể null nếu đăng ký bằng phone
        String phone,        // có thể null nếu đăng ký bằng email
        String password,     // bắt buộc
        String fullName,     // bắt buộc
        Role role            // cho phép JOBSEEKER | EMPLOYER; null => JOBSEEKER
) {}
