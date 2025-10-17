package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company_reviews", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "job_seeker_profile_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyReview {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private UUID reviewId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;

    @Column(nullable = false)
    private Double rating; // 0.0 - 5.0

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String pros;

    @Column(columnDefinition = "TEXT")
    private String cons;

    @Column(name = "recommend_to_friend")
    private Boolean recommendToFriend;

    @Column(length = 20)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
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
}
