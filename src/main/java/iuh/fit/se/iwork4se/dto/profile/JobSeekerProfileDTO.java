package iuh.fit.se.iwork4se.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobSeekerProfileDTO {
    private UUID id;
    private String headline;
    private String summary;
    private String location;
    private int yearsExp;
    private UUID userId;
}
