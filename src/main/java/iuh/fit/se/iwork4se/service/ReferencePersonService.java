package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.ReferencePersonDTO;

import java.util.List;
import java.util.UUID;

public interface ReferencePersonService {
    List<ReferencePersonDTO> listMyReferences(UUID currentUserId);
    ReferencePersonDTO createMyReference(UUID currentUserId, String name, String company, String email, String relation);
    ReferencePersonDTO updateMyReference(UUID currentUserId, UUID referenceId, String name, String company, String email, String relation);
    void deleteMyReference(UUID currentUserId, UUID referenceId);
}
