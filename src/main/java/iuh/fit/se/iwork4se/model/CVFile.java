package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cv_files")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CVFile {

    @Id
    @GeneratedValue
    private UUID cVId;

    @Column(nullable = false)
    private UUID ownerUserId;

    @Column(nullable = false, length = 255)
    private String objectKey;

    @Column(nullable = false, length = 150)
    private String fileName;

    @Column(length = 50)
    private String mimeType;

    private long size;
    private Instant uploadedAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
