package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "reference_persons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReferencePerson {

    @Id
    @GeneratedValue
    @Column(name = "reference_person_id")
    private UUID referencePersonId;

    @Column(length = 120)
    private String name;

    @Column(length = 150)
    private String company;

    @Column(length = 150)
    private String email;

    @Column(length = 80)
    private String relation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false) // << yêu cầu
    private JobSeekerProfile jobSeekerProfile;
}
