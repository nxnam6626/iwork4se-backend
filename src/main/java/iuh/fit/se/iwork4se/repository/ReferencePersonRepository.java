package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.ReferencePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReferencePersonRepository extends JpaRepository<ReferencePerson, UUID> {
    List<ReferencePerson> findByJobSeekerProfile_JobSeekerProfileId(UUID jobSeekerProfileId);
}
