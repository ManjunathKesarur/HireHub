package com.lancer.HireHub.service;

import java.util.List;

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
	
	
	public List<JobApplication> getAllApplication(JobApplication application) {
		return jobApplicationRepository.findAll();
	}
	
	
	public JobApplication getApplicationById(Integer id) {
	    return jobApplicationRepository.findById(id).orElse(null);
	}
	
	
	public JobApplication updateStatus(Integer id, String status) {

	    JobApplication application = jobApplicationRepository.findById(id).orElse(null);

	    if (application != null) {
	        application.setStatus(status);
	        return jobApplicationRepository.save(application);
	    }

	    return null;
	}
}
