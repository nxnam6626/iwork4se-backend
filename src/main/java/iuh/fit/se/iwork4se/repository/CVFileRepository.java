package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.CVFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CVFileRepository extends JpaRepository<CVFile, UUID> {
    List<CVFile> findByOwnerUserId(UUID ownerUserId);
    Optional<CVFile> findByObjectKey(String objectKey);
    
    // Kiểm tra trùng lặp: cùng fileName
    boolean existsByOwnerUserIdAndFileName(UUID ownerUserId, String fileName);
}
