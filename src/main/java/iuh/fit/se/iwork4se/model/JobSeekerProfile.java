package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_seeker_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobSeekerProfile {

    @Id
    @GeneratedValue
    @Column(name = "job_seeker_profile_id")
    private UUID jobSeekerProfileId;

    @Column(length = 150)
    private String headline;

    @Column(length = 1000)
    private String summary;

    @Column(length = 120)
    private String location;

    @Column(nullable = false)
    private int yearsExp = 0;

    // Mỗi user có đúng 1 profile
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Quan hệ ngược (tuỳ chọn nhưng tiện cho cascade/orphanRemoval)
    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReferencePerson> references;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeekerSkill> seekerSkills;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes;
}
