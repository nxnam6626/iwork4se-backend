package iuh.fit.se.iwork4se.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDetailDTO {
    private UUID profileId;
    private String fullName;
    private String headline;
    private String summary;
    private String location;
    private int yearsExp;
    private List<String> skills; // name-level formatted
    private List<String> experiences; // title @ company (start - end)
    private List<String> educations; // degree at school (years)
}


