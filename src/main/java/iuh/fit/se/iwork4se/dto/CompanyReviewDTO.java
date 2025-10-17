package iuh.fit.se.iwork4se.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyReviewDTO {

    private UUID id;
    private UUID companyId;
    private String companyName;
    private UUID jobSeekerProfileId;
    private String jobSeekerName;
    private Double rating;
    private String comment;
    private String title;
    private String pros;
    private String cons;
    private Boolean recommendToFriend;
    private String status;
    private String adminNotes;
    private Instant createdAt;
    private Instant updatedAt;
}
