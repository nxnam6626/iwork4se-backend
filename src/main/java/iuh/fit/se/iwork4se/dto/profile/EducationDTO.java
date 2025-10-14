package iuh.fit.se.iwork4se.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationDTO {
    private UUID id;
    private String school;
    private String degree;
    private String major;
    private LocalDate startYear;
    private LocalDate endYear;
    private String description;
    private UUID profileId;
}
