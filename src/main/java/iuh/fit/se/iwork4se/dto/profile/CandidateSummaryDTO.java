package iuh.fit.se.iwork4se.dto.profile;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSummaryDTO {
    private UUID profileId;
    private String headline;
    private String location;
    private int yearsExp;
    private List<String> topSkills;
    private List<String> recentTitles;
}


