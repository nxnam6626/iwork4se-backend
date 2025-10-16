package iuh.fit.se.iwork4se.controller;

import iuh.fit.se.iwork4se.dto.CompanyDTO;
import iuh.fit.se.iwork4se.dto.CompanySearchRequest;
import iuh.fit.se.iwork4se.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> getById(@PathVariable UUID companyId) {
        return ResponseEntity.ok(companyService.getCompany(companyId));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CompanyDTO>> search(@Valid @RequestBody CompanySearchRequest request) {
        return ResponseEntity.ok(companyService.searchCompanies(request));
    }
}


