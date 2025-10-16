package iuh.fit.se.iwork4se.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private UUID companyId;
    private String name;
    private String industry;
    private String description;
    private String headquarters;
    private String size;
    private Integer foundedYear;
    private String logoUrl;
}


