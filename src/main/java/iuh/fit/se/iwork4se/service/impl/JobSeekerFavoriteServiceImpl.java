package iuh.fit.se.iwork4se.service.impl;

import iuh.fit.se.iwork4se.dto.*;
import iuh.fit.se.iwork4se.exception.ForbiddenException;
import iuh.fit.se.iwork4se.exception.ResourceNotFoundException;
import iuh.fit.se.iwork4se.model.JobFavorite;
import iuh.fit.se.iwork4se.repository.JobFavoriteRepository;
import iuh.fit.se.iwork4se.repository.JobRepository;
import iuh.fit.se.iwork4se.repository.JobSeekerProfileRepository;
import iuh.fit.se.iwork4se.security.SecurityUtil;
import iuh.fit.se.iwork4se.service.JobSeekerFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobSeekerFavoriteServiceImpl implements JobSeekerFavoriteService {

    private final JobFavoriteRepository jobFavoriteRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    @Override
    public JobFavoriteDTO addToFavorites(UUID jobId, AddToFavoritesRequest request) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        // Lấy job seeker profile
        var jobSeekerProfile = jobSeekerProfileRepository.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        
        // Kiểm tra job có tồn tại không
        var job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        // Kiểm tra job đã được yêu thích chưa
        if (jobFavoriteRepository.existsByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(
                jobSeekerProfile.getJobSeekerProfileId(), jobId)) {
            throw new ForbiddenException("Job is already in favorites");
        }
        
        // Tạo job favorite
        var jobFavorite = JobFavorite.builder()
                .jobSeekerProfile(jobSeekerProfile)
                .job(job)
                .notes(request.notes())
                .build();
        
        jobFavorite = jobFavoriteRepository.save(jobFavorite);
        
        return toJobFavoriteDTO(jobFavorite);
    }

    @Override
    public void removeFromFavorites(UUID jobId) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobSeekerProfile = jobSeekerProfileRepository.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        
        if (!jobFavoriteRepository.existsByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(
                jobSeekerProfile.getJobSeekerProfileId(), jobId)) {
            throw new ResourceNotFoundException("Job is not in favorites");
        }
        
        jobFavoriteRepository.deleteByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(
                jobSeekerProfile.getJobSeekerProfileId(), jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobFavoriteSummaryDTO> getMyFavorites(int page, int size) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobSeekerProfile = jobSeekerProfileRepository.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<JobFavorite> favorites = jobFavoriteRepository.findWithJobAndCompanyByJobSeekerProfileId(
                jobSeekerProfile.getJobSeekerProfileId(), pageable);
        
        return favorites.map(this::toJobFavoriteSummaryDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public JobFavoriteDTO getFavoriteDetail(UUID favoriteId) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobFavorite = jobFavoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        
        // Kiểm tra quyền sở hữu
        if (!jobFavorite.getJobSeekerProfile().getUser().getUserId().equals(currentUserId)) {
            throw new ForbiddenException("Access denied to this favorite");
        }
        
        return toJobFavoriteDTO(jobFavorite);
    }

    @Override
    public JobFavoriteDTO updateFavoriteNotes(UUID favoriteId, UpdateFavoriteNotesRequest request) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobFavorite = jobFavoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        
        // Kiểm tra quyền sở hữu
        if (!jobFavorite.getJobSeekerProfile().getUser().getUserId().equals(currentUserId)) {
            throw new ForbiddenException("Access denied to this favorite");
        }
        
        jobFavorite.setNotes(request.notes());
        jobFavorite = jobFavoriteRepository.save(jobFavorite);
        
        return toJobFavoriteDTO(jobFavorite);
    }

    @Override
    @Transactional(readOnly = true)
    public FavoriteStatusDTO getFavoriteStatus(UUID jobId) {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobSeekerProfile = jobSeekerProfileRepository.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        
        var jobFavorite = jobFavoriteRepository.findByJobSeekerProfile_JobSeekerProfileIdAndJob_JobId(
                jobSeekerProfile.getJobSeekerProfileId(), jobId);
        
        return FavoriteStatusDTO.builder()
                .isFavorited(jobFavorite.isPresent())
                .favoriteId(jobFavorite.map(JF -> JF.getId()).orElse(null))
                .favoritedAt(jobFavorite.map(JF -> JF.getFavoritedAt()).orElse(null))
                .notes(jobFavorite.map(JF -> JF.getNotes()).orElse(null))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long getFavoriteCount() {
        UUID currentUserId = SecurityUtil.currentUserId();
        
        var jobSeekerProfile = jobSeekerProfileRepository.findByUser_UserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker profile not found"));
        
        return jobFavoriteRepository.countByJobSeekerProfile_JobSeekerProfileId(
                jobSeekerProfile.getJobSeekerProfileId());
    }

    private JobFavoriteDTO toJobFavoriteDTO(JobFavorite jobFavorite) {
        var job = jobFavorite.getJob();
        var company = job.getCompany();
        
        return JobFavoriteDTO.builder()
                .id(jobFavorite.getId())
                .jobId(job.getJobId())
                .jobTitle(job.getTitle())
                .companyName(company.getName())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .favoritedAt(jobFavorite.getFavoritedAt())
                .notes(jobFavorite.getNotes())
                .jobDescription(job.getDescription())
                .companyIndustry(company.getIndustry())
                .build();
    }

    private JobFavoriteSummaryDTO toJobFavoriteSummaryDTO(JobFavorite jobFavorite) {
        var job = jobFavorite.getJob();
        var company = job.getCompany();
        
        return JobFavoriteSummaryDTO.builder()
                .id(jobFavorite.getId())
                .jobId(job.getJobId())
                .jobTitle(job.getTitle())
                .companyName(company.getName())
                .location(job.getLocation())
                .employmentType(job.getEmploymentType())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .favoritedAt(jobFavorite.getFavoritedAt())
                .notes(jobFavorite.getNotes())
                .build();
    }
}
