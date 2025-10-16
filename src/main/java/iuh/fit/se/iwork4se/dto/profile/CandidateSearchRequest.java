package iuh.fit.se.iwork4se.dto.profile;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CandidateSearchRequest {
    private String keyword; // headline/summary/location
    @Min(0)
    private Integer minYearsExp;
    private String location;
    private List<String> titles; // job titles experienced
    private List<String> skills; // skill names

    @Min(0)
    private Integer page = 0;
    @Min(1)
    private Integer size = 10;
}


