package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.profile.CVFileDTO;

import java.util.List;
import java.util.UUID;

public interface CVFileService {
    CVFileDTO createMyCVFile(UUID currentUserId, UUID resumeId, String objectKey, String fileName, String mimeType, long size);
    List<CVFileDTO> listMyCVFiles(UUID currentUserId);
    CVFileDTO getMyCVFile(UUID currentUserId, UUID cvFileId);
    void deleteMyCVFile(UUID currentUserId, UUID cvFileId);
}
