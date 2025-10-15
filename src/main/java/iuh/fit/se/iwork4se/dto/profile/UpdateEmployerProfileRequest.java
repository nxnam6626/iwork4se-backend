package iuh.fit.se.iwork4se.dto.profile;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployerProfileRequest {
    @Size(max = 200)
    private String companyName;

    @Size(max = 100)
    private String industry;

    @Size(max = 500)
    private String description;

    @Size(max = 200)
    private String headquarters;

    @Size(max = 100)
    private String size;

    private Integer foundedYear;

    // Contact person info
    @Size(max = 150)
    private String title;

    @Size(max = 100)
    private String department;

    @Size(max = 150)
    private String contactEmail;

    @Size(max = 30)
    private String contactPhone;
}


