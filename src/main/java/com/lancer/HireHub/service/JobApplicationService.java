package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;


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
			jobApplication.setUser(credential);
			jobApplication.setStatus("APPLIED");
			
			
			
		return	jobApplicationRepository.save(jobApplication);
			
		}
		
		else {
			throw new EmailAlreadyExistException("not job found on jobid "+jobApplicationDto.getJobId());   // will change later
		}
		
	}
	
	
	public List<JobApplication> getAllApplication(Integer pageNumber,Integer pageSize,String field) {
		
		Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User loggedUser = userRepository.findByEmail(email)
		        .orElseThrow(() ->
		                new EmailAlreadyExistException("User not found"));
		
		if (!"ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
		    throw new EmailAlreadyExistException(
		            "Only admin can access all applications");
		}
		
		Sort sortz=Sort.by(field).ascending();
		Pageable pageable= PageRequest.of(pageNumber,pageSize, sortz);
		Page<JobApplication> pa	=jobApplicationRepository.findAll(pageable);
		return pa.getContent();
	}
	
	
	public JobApplication getApplicationById(Integer id) {
			JobApplication application = jobApplicationRepository.findById(id).orElseThrow(()-> new EmailAlreadyExistException("application not found"));
	
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		
			User loggedUser = userRepository.findByEmail(email).orElseThrow(()-> new EmailAlreadyExistException("User not found"));			
				
		if("ADMIN".equalsIgnoreCase(loggedUser.getRole()))
			return application;
		
		if("RECRUITER".equalsIgnoreCase(loggedUser.getRole())) {
			
			if(application.getJob().getUser()==null  ||	!application.getJob().getUser().getId().equals(loggedUser.getId())) {
				throw new EmailAlreadyExistException("You can't access other's application");
			}
			return application;
		}
		
		if("JOB_SEEKER".equalsIgnoreCase(loggedUser.getRole())) {
			
			if(!application.getUser().getId().equals(loggedUser.getId())) {
				throw new EmailAlreadyExistException("only your application you can access");
			}
			return application;
		}
			
		throw new EmailAlreadyExistException("Access denied");
	}
	
	
	
	public JobApplication updateStatus(Integer id, String status) {

	    JobApplication application = jobApplicationRepository.findById(id).orElseThrow(()-> new EmailAlreadyExistException("the application is not exists"));
	    
	    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	    
	    String email = authentication.getName();
	    
	    User loggedUser=userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException("enter ceredential is not present in db"));
	
	    
	    if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
	    	application.setStatus(status);
	    	return jobApplicationRepository.save(application);
	    }
	    
	    if(loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER")) {
	    	
	    	throw new EmailAlreadyExistException("Job seeker cant update status");
	    }
	    
	    if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")){
	    	
	    	if(	application.getJob().getUser()==null ||	!loggedUser.getId().equals(application.getJob().getUser().getId())  ) {
	    		throw new EmailAlreadyExistException("you cant check others application");
	    	}
	    	
	    	if ("SELECTED".equalsIgnoreCase(application.getStatus())) {
	    	    throw new EmailAlreadyExistException("Selected candidate cannot be modified");
	    	}

	    	if ("REJECTED".equalsIgnoreCase(application.getStatus())) {
	    	    throw new EmailAlreadyExistException("Rejected candidate cannot be modified");
	    	}

	    	if (application.getStatus().equalsIgnoreCase(status)) {
	    	    throw new EmailAlreadyExistException("Application is already " + status);
	    	}
	    	application.setStatus(status);
	    	return jobApplicationRepository.save(application);
	    }
	    
	    throw new EmailAlreadyExistException("USER NOT FOUND");
	    
	}
	
	
	
	public String deleteApplication(Integer id) {
		
		JobApplication application=	jobApplicationRepository.findById(id).orElseThrow(()->new EmailAlreadyExistException("USER Not Found"));
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
	User loggedUser	= userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException(email+" :not found"));
	
	if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
		jobApplicationRepository.deleteById(id);
		return "Deleted successfully";
	}
	
	if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
		
				if(application.getJob().getUser()==null	||	!application.getJob().getUser().getId().equals(loggedUser.getId()) ) {
						throw new EmailAlreadyExistException(" Mr " +email+" You Can't Delete Others Application");
				}
		jobApplicationRepository.deleteById(id);
		return "Deleted The Application "+id;
	}
	
	    throw new EmailAlreadyExistException("ACCESS DENIED");
	}
	
	
	
	public List<JobApplication> getApplicationByUser() {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			String credentials = authentication.getName();
		
			Optional<User> userinfo=	userRepository.findByEmail(credentials);
			
			if(userinfo.isPresent()) {
					User getuserinfo =userinfo.get();
					
					if (!"JOB_SEEKER".equalsIgnoreCase(getuserinfo.getRole())) {
							throw new EmailAlreadyExistException("Access denied for admin and recruiter");
					}
					
					if(jobApplicationRepository.findByUser_Id(getuserinfo.getId()).isEmpty()) {
						throw new EmailAlreadyExistException("the user not applied any job yet");
					}
					
					return jobApplicationRepository.findByUser_Id(getuserinfo.getId());
			}else
				throw new EmailAlreadyExistException("denied");								//will change later
	}
	
	public List<JobApplication> getApplicationByJob(Integer jobid){
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			
			String email = authentication.getName();
		
			User loggeduser=userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException("User Not found"));
				
			Job job = jobRepository.findById(jobid).orElseThrow(()->new EmailAlreadyExistException("Job Not Found"));
			
			if(!"RECRUITER".equalsIgnoreCase(loggeduser.getRole()) && !"ADMIN".equalsIgnoreCase(loggeduser.getRole())) {
				throw new EmailAlreadyExistException("only admin and recruiter");
			}
			
			if("RECRUITER".equalsIgnoreCase(loggeduser.getRole()) && !job.getUser().getId().equals(loggeduser.getId())) {	
				throw new EmailAlreadyExistException("you can aceess your own job application");
			}		
			
		return jobApplicationRepository.findByJob_Id(jobid);
	}
	
	
}
