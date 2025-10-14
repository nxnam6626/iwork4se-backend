package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_phone", columnNames = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID userId;

    // Cho phép null để hỗ trợ đăng ký chỉ bằng phone
    @Email
    @Size(max = 150)
    @Column(name = "email", unique = true, length = 150)
    private String email;

    // Cho phép null để hỗ trợ đăng ký chỉ bằng email
    // Khuyến nghị chuẩn hoá E.164 (+84…) trước khi lưu
    @Size(max = 30)
    @Column(name = "phone", unique = true, length = 30)
    private String phone;

    @Column(name = "password_hash", nullable = false, length = 300)
    private String passwordHash;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(length = 300)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role = Role.JOBSEEKER;

    // Cờ xác minh — tuỳ quy trình thực tế
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;


    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.updatedAt == null) this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    // Ít nhất một trong hai phải có: email hoặc phone
    @AssertTrue(message = "Either email or phone must be provided")
    public boolean isEmailOrPhoneProvided() {
        return (email != null && !email.isBlank()) || (phone != null && !phone.isBlank());
    }

    // Quan hệ 1-1 ngược lại (tùy chọn). Nếu cần truy cập từ User -> Profile:
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private JobSeekerProfile jobSeekerProfile;
}
