package iuh.fit.se.iwork4se.dto.profile;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerProfileDTO {
    private UUID employerProfileId;
    private UUID userId;
    private UUID companyId;
    private String companyName;
    private String title;
    private String department;
    private String contactEmail;
    private String contactPhone;
}


