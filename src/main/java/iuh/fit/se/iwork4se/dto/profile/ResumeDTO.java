package iuh.fit.se.iwork4se.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeDTO {
    private UUID id;
    private String title;
    private Instant updatedAt;
    private UUID jobSeekerProfileId;
    private UUID currentFileId;
}
