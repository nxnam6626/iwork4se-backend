package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue
    @Column(name = "company_id")
    private UUID companyId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String industry;

    @Column(length = 500)
    private String description;

    @Column(length = 200)
    private String headquarters;

    @Column(length = 100)
    private String size; // e.g., 11-50, 51-200

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

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


