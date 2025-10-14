package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "educations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Education {

    @Id
    @GeneratedValue
    @Column(name = "education_id")
    private UUID educationId;

    @Column(length = 150)
    private String school;

    @Column(length = 150)
    private String degree;

    @Column(length = 150)
    private String major;

    private LocalDate startYear;
    private LocalDate endYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false) // << yêu cầu
    private JobSeekerProfile jobSeekerProfile;
}
