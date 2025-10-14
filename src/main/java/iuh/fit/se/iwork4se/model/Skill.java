package iuh.fit.se.iwork4se.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "skills",
        uniqueConstraints = @UniqueConstraint(name = "uk_skill_name", columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {

    @Id
    @GeneratedValue
    @Column(name = "skill_id")
    private UUID skillId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 300)
    private String description;
}
