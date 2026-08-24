package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.entity.JobApplication;
import com.lancer.HireHub.exception.EmailAlreadyExistException;
import com.lancer.HireHub.repository.JobApplicationRepository;
import com.lancer.HireHub.repository.JobRepository;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class JobApplicationService {

	@Autowired
	JobApplicationRepository jobApplicationRepository;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	
	public JobApplication applyjob(JobApplication jobApplication) {
		
		if(!userRepository.existsById(jobApplication.getUser().getId()))
			throw new EmailAlreadyExistException("User Is Not Registerd Or Invalid UserId : "+jobApplication.getUser().getId());
		
		Optional<Job> jobz=	jobRepository.findById(jobApplication.getJob().getId());
		
		if(jobz.isPresent()) {
			Job po=	jobz.get();
			
			if(po.getStatus().equalsIgnoreCase("CLOSE"))
				throw new EmailAlreadyExistException("application is closed");      ////same
			
			if(jobApplicationRepository.existsByUser_IdAndJob_Id(jobApplication.getUser().getId(),jobApplication.getJob().getId()))
				throw new EmailAlreadyExistException("already applied for this JobId: "+jobApplication.getJob().getId());
			
			jobApplication.setStatus("APPLIED");
			
		return	jobApplicationRepository.save(jobApplication);
			
		}
		
		else {
			throw new EmailAlreadyExistException("not job found on jobid "+jobApplication.getJob().getId());   // will change later
		}
		
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
			return	jobApplicationRepository.findByUser_Id(userid);
	}
	
	public List<JobApplication> getApplicationByJob(Integer jobid){
		return jobApplicationRepository.findByJob_Id(jobid);
	}
	
	public Boolean hasApplication(Integer userid ,Integer jobid) {
		return jobApplicationRepository.existsByUser_IdAndJob_Id(userid,jobid);
	}
	
	public List<JobApplication> getApplicationByStatus(String status){
		return jobApplicationRepository.findByStatus(status);
	}
}
