package iuh.fit.se.iwork4se.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferencePersonDTO {
    private UUID id;
    private String name;
    private String company;
    private String email;
    private String relation;
    private UUID jobSeekerProfileId;
}
