package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.SkillDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillService {
    SkillDTO create(String name, String description);
    Optional<SkillDTO> findByName(String name);
    List<SkillDTO> listAll();
    SkillDTO get(UUID id);
}
