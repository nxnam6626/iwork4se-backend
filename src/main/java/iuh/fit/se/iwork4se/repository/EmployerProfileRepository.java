package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, UUID> {
    Optional<EmployerProfile> findByUser_UserId(UUID userId);
}


