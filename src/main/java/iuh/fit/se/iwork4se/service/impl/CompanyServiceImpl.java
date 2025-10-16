package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.CompanyDTO;
import iuh.fit.se.iwork4se.dto.CompanySearchRequest;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.Company;
import iuh.fit.se.iwork4se.repository.CompanyRepository;
import iuh.fit.se.iwork4se.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyDTO getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return toDTO(company);
    }

    @Override
    public Page<CompanyDTO> searchCompanies(CompanySearchRequest request) {
        String keyword = request.getKeyword() == null ? "" : request.getKeyword();
        var pageable = PageRequest.of(Math.max(request.getPage(), 0), Math.max(request.getSize(), 1));
        var page = companyRepository
                .findByNameContainingIgnoreCaseOrIndustryContainingIgnoreCase(keyword, keyword, pageable);
        List<CompanyDTO> mapped = page.getContent().stream().map(this::toDTO).toList();
        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }

    private CompanyDTO toDTO(Company c) {
        return CompanyDTO.builder()
                .companyId(c.getCompanyId())
                .name(c.getName())
                .industry(c.getIndustry())
                .description(c.getDescription())
                .headquarters(c.getHeadquarters())
                .size(c.getSize())
                .foundedYear(c.getFoundedYear())
                .logoUrl(c.getLogoUrl())
                .build();
    }
}


