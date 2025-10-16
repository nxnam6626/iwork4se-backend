package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.SeekerSkillDTO;

import java.util.List;
import java.util.UUID;

public interface SeekerSkillService {
    List<SeekerSkillDTO> listMySeekerSkills(UUID currentUserId);
    SeekerSkillDTO addMySkill(UUID currentUserId, String skillName, String level);
    SeekerSkillDTO updateMySkillLevel(UUID currentUserId, UUID seekerSkillId, String level);
    void removeMySkill(UUID currentUserId, UUID seekerSkillId);
}
