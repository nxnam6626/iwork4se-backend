package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.profile.CandidateSearchRequest;
import iuh.fit.se.iwork4se.dto.profile.CandidateSummaryDTO;
import iuh.fit.se.iwork4se.model.JobSeekerProfile;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.service.CandidateSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateSearchServiceImpl implements CandidateSearchService {
    private final JobSeekerProfileRepository profileRepository;

    @Override
    @PreAuthorize("hasRole('EMPLOYER')")
    public Page<CandidateSummaryDTO> searchCandidates(CandidateSearchRequest request) {
        var page = PageRequest.of(request.getPage(), request.getSize());

        List<String> titles = request.getTitles() == null ? List.of() : request.getTitles().stream()
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .toList();
        List<String> skills = request.getSkills() == null ? List.of() : request.getSkills().stream()
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .toList();

        var result = profileRepository.search(
                blankToNull(request.getKeyword()),
                request.getMinYearsExp(),
                blankToNull(request.getLocation()),
                titles.isEmpty() ? null : titles,
                titles.isEmpty(),
                skills.isEmpty() ? null : skills,
                skills.isEmpty(),
                page
        );


        return result.map(CandidateSearchServiceImpl::toSummary);
    }

    private static CandidateSummaryDTO toSummary(JobSeekerProfile p) {
        List<String> topSkills = p.getSeekerSkills() == null ? List.of() : p.getSeekerSkills().stream()
                .limit(5)
                .map(ss -> ss.getSkill().getName())
                .collect(Collectors.toList());
        List<String> titles = p.getExperiences() == null ? List.of() : p.getExperiences().stream()
                .limit(3)
                .map(exp -> exp.getTitle())
                .collect(Collectors.toList());

        return CandidateSummaryDTO.builder()
                .profileId(p.getJobSeekerProfileId())
                .headline(p.getHeadline())
                .location(p.getLocation())
                .yearsExp(p.getYearsExp())
                .topSkills(topSkills)
                .recentTitles(titles)
                .build();
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}


