package iuh.fit.se.iwork4se.dto.profile;

import iuh.fit.se.iwork4se.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull
    private ApplicationStatus toStatus;
    private String note;
}


