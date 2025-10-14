package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "seeker_skills",
        uniqueConstraints = @UniqueConstraint(name = "uk_profile_skill",
                columnNames = {"job_seeker_profile_id", "skill_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeekerSkill {

    @Id
    @GeneratedValue
    @Column(name = "seeker_skill_id")
    private UUID seekerSkillId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false) // << yêu cầu
    private JobSeekerProfile jobSeekerProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(length = 40)
    private String level; // BEGINNER/INTERMEDIATE/ADVANCED/EXPERT...
}
