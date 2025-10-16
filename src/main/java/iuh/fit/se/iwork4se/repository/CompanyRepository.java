package iuh.fit.se.iwork4se.repository;

import iuh.fit.se.iwork4se.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByNameIgnoreCase(String name);

    Page<Company> findByNameContainingIgnoreCaseOrIndustryContainingIgnoreCase(
            String nameKeyword,
            String industryKeyword,
            Pageable pageable
    );
}


