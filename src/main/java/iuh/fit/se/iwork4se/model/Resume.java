package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resumes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resume {

    @Id
    @GeneratedValue
    @Column(name = "resume_id")
    private UUID resumeId;

    @Column(length = 150)
    private String title;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false) // << yêu cầu
    private JobSeekerProfile jobSeekerProfile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_file_id")
    private CVFile currentFile;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CVFile> files;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
