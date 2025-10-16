package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.CompanyDTO;
import iuh.fit.se.iwork4se.dto.CompanySearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CompanyService {
    CompanyDTO getCompany(UUID companyId);

    Page<CompanyDTO> searchCompanies(CompanySearchRequest request);
}


