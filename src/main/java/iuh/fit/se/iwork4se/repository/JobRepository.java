package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
    Page<Job> findByCompany_CompanyIdOrderByCreatedAtDesc(UUID companyId, Pageable pageable);
}


