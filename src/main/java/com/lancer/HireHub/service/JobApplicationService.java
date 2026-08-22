package com.lancer.HireHub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	
	public List<JobApplication> getAllApplication(Integer pageNumber,Integer pageSize,String field) {
		Sort sortz=Sort.by(field).ascending();
		Pageable pageable= PageRequest.of(pageNumber,pageSize, sortz);
		Page<JobApplication> pa	=jobApplicationRepository.findAll(pageable);
		return pa.getContent();
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
	
	
	public void deleteApplication(Integer id) {
	    jobApplicationRepository.deleteById(id);
	}
	
	
	public List<JobApplication> getApplicationByUser(Integer userid) {
			return	jobApplicationRepository.findByUserid(userid);
	}
	
	public List<JobApplication> getApplicationByJob(Integer jobid){
		return jobApplicationRepository.findByJobid(jobid);
	}
	
	public Boolean hasApplication(Integer jobid) {
		return jobApplicationRepository.existsByJobid(jobid);
	}
	
	public List<JobApplication> getApplicationByStatus(String status){
		return jobApplicationRepository.findByStatus(status);
	}
}
