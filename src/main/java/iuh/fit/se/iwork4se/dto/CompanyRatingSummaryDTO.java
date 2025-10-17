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
public class CompanyRatingSummaryDTO {

    private UUID companyId;
    private String companyName;
    private Double averageRating;
    private Long totalReviews;
}
