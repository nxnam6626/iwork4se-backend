package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "experiences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Experience {

    @Id
    @GeneratedValue
    @Column(name = "experience_id")
    private UUID experienceId;

    @Column(length = 150)
    private String companyName;

    @Column(length = 150)
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isCurrent = false;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false) // << yêu cầu
    private JobSeekerProfile jobSeekerProfile;
}
