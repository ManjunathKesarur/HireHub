package com.lancer.HireHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.entity.JobApplication;
import com.lancer.HireHub.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

	@Autowired
	JobApplicationRepository jobApplicationRepository;
	
	public JobApplication applyjob(JobApplication jobApplication) {
		jobApplication.setStatus("applied");
			return jobApplicationRepository.save(jobApplication);
	}
}
