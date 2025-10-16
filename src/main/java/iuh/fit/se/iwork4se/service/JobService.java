package iuh.fit.se.iwork4se.service;

import iuh.fit.se.iwork4se.dto.JobDTO;

import java.util.UUID;

public interface JobService {
    JobDTO getJobDetail(UUID jobId);
}


