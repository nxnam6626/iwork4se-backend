package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "job_favorites", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"job_seeker_profile_id", "job_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant favoritedAt;

    @Column(length = 500)
    private String notes; // Ghi chú cá nhân về job này
}
