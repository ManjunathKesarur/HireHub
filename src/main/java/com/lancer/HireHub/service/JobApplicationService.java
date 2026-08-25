package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.dto.JobApplicationDto;
import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.entity.JobApplication;
import com.lancer.HireHub.entity.User;
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
	
	
	
	public JobApplication applyjob(JobApplicationDto jobApplicationDto) {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String email=	authentication.getName();
		Optional<User> usez=userRepository.findByEmail(email);
			User credential =	usez.get();
		
		
		
		Optional<Job> jobz=	jobRepository.findById(jobApplicationDto.getJobId());
		
		if(jobz.isPresent()) {
			Job jobcredential=	jobz.get();
			
			if(jobcredential.getStatus().equalsIgnoreCase("CLOSED"))
				throw new EmailAlreadyExistException("application is closed");      ////same
			
			if(jobApplicationRepository.existsByUser_IdAndJob_Id(credential.getId(),
																	jobApplicationDto.getJobId()))
				throw new EmailAlreadyExistException("already applied for this JobId: "+jobApplicationDto.getJobId());
			
	
			JobApplication jobApplication=new JobApplication();
			
			jobApplication.setJob(jobcredential);
			jobApplication.setUser(credential);;
			jobApplication.setStatus("APPLIED");
			
			
			
		return	jobApplicationRepository.save(jobApplication);
			
		}
		
		else {
			throw new EmailAlreadyExistException("not job found on jobid "+jobApplicationDto.getJobId());   // will change later
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
	
	
	public List<JobApplication> getApplicationByUser() {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			String credentials = authentication.getName();
		
			Optional<User> userinfo=	userRepository.findByEmail(credentials);
			
			if(userinfo.isPresent()) {
					User getuserinfo =userinfo.get();
					
					if (jobApplicationRepository.findByUser_Id(getuserinfo.getId()).isEmpty()) {
							throw new EmailAlreadyExistException("Access denied for admin and recruiter");
					}
					
					return jobApplicationRepository.findByUser_Id(getuserinfo.getId());
			}else
				throw new EmailAlreadyExistException("denied");								//will change later
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
