package iuh.fit.se.iwork4se.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplyJobRequest {
    @Size(max = 500)
    private String note; // optional cover letter or note
}
