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
import com.lancer.HireHub.exception.AccessDeniedException;
import com.lancer.HireHub.exception.AlreadyExistsException;
import com.lancer.HireHub.exception.ResourceNotFoundException;
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
	
	@Autowired
	EmailService emailService;
	
	
	public JobApplication applyjob(JobApplicationDto jobApplicationDto) {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String email=	authentication.getName();
		User credential = userRepository.findByEmail(email)
		        .orElseThrow(() ->
		            new ResourceNotFoundException("User not found"));
		
		Optional<Job> jobz=	jobRepository.findById(jobApplicationDto.getJobId());
		
		if(jobz.isPresent()) {
			Job jobcredential=	jobz.get();
			
			if(jobcredential.getStatus().equalsIgnoreCase("CLOSED"))
				throw new ResourceNotFoundException("application is closed");     
			
			if(jobApplicationRepository.existsByUser_IdAndJob_Id(credential.getId(),
																	jobApplicationDto.getJobId()))
				throw new AlreadyExistsException("already applied for this JobId: "+jobApplicationDto.getJobId());
			
	
			JobApplication jobApplication=new JobApplication();
			
			jobApplication.setJob(jobcredential);
			jobApplication.setUser(credential);
			jobApplication.setStatus("APPLIED");
			
		return	jobApplicationRepository.save(jobApplication);
			
		}
		
		else {
			throw new AccessDeniedException("not job found on jobid "+jobApplicationDto.getJobId());  
		}
		
	}
	
	
	
	public List<JobApplication> getAllApplication(Integer pageNumber,Integer pageSize,String field) {
		
		Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User loggedUser = userRepository.findByEmail(email)
		        .orElseThrow(() ->
		                new ResourceNotFoundException("User not found"));
		
		if (!"ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
		    throw new AccessDeniedException(
		            "Only admin can access all applications");
		}
		
		Sort sortz=Sort.by(field).ascending();
		Pageable pageable= PageRequest.of(pageNumber,pageSize, sortz);
		Page<JobApplication> pa	=jobApplicationRepository.findAll(pageable);
		return pa.getContent();
	}
	
	
	
	public JobApplication getApplicationById(Integer id) {
			JobApplication application = jobApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("application not found"));
	
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		
			User loggedUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));			
				
		if("ADMIN".equalsIgnoreCase(loggedUser.getRole()))
			return application;
		
		if("RECRUITER".equalsIgnoreCase(loggedUser.getRole())) {
			
			if(application.getJob().getUser()==null  ||	!application.getJob().getUser().getId().equals(loggedUser.getId())) {
				throw new AccessDeniedException("You can't access other's application");
			}
			return application;
		}
		
		if("JOB_SEEKER".equalsIgnoreCase(loggedUser.getRole())) {
			
			if(!application.getUser().getId().equals(loggedUser.getId())) {
				throw new AccessDeniedException("only your application you can access");
			}
			return application;
		}
			
		throw new AccessDeniedException("Access denied");
	}
	
	
	
	public JobApplication updateStatus(Integer id, String status) {

	    JobApplication application = jobApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("the application is not exists"));
	    
	    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	    
	    String email = authentication.getName();
	    
	    User loggedUser=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("enter ceredential is not present in db"));
	
	    if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
	    	application.setStatus(status);
	    	return jobApplicationRepository.save(application);
	    }
	    
	    if(loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER")) {
	    	
	    	throw new AccessDeniedException("Job seeker cant update status");
	    }
	    
	    if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")){
	    	
	    	if(	application.getJob().getUser()==null ||	!loggedUser.getId().equals(application.getJob().getUser().getId())  ) {
	    		throw new AccessDeniedException("you cant check others application");
	    	}
	    	
	    	if ("SELECTED".equalsIgnoreCase(application.getStatus())) {
	    	    throw new AlreadyExistsException("Selected candidate cannot be modified");
	    	}

	    	if ("REJECTED".equalsIgnoreCase(application.getStatus())) {
	    	    throw new ResourceNotFoundException("Rejected candidate cannot be modified");
	    	}

	    	if (application.getStatus().equalsIgnoreCase(status)) {
	    	    throw new AlreadyExistsException("Application is already " + status);
	    	}
	    	
	    	if(status.equalsIgnoreCase("SELECTED")) {
	    	application.setStatus(status);
	    	
	    	emailService.mailMessage(application.getUser().getEmail(),
	    			application.getJob().getCompany(),
	    			"Congraguation You Are Selected to the "+application.getJob().getTitle()+" role in our company "
	    			+application.getJob().getCompany());   
	    	
	    	return jobApplicationRepository.save(application);
	    	}
	    	
	    	application.setStatus(status);
	    	
	    	return jobApplicationRepository.save(application);
	    }
	    
	    throw new AccessDeniedException("USER NOT FOUND");
	    
	}
	
	
	
	public String deleteApplication(Integer id) {
		
		JobApplication application=	jobApplicationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("USER Not Found"));
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
	User loggedUser	= userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException(email+" :not found"));
	
	if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
		jobApplicationRepository.deleteById(id);
		return "Deleted successfully";
	}
	
	if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
		
				if(application.getJob().getUser()==null	||	!application.getJob().getUser().getId().equals(loggedUser.getId()) ) {
						throw new AccessDeniedException(" Mr " +email+" You Can't Delete Others Application");
				}
		jobApplicationRepository.deleteById(id);
		return "Deleted The Application "+id;
	}
	
	    throw new AccessDeniedException("ACCESS DENIED");
	}
	
	
	
	public List<JobApplication> getApplicationByUser() {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			String credentials = authentication.getName();
		
			Optional<User> userinfo=	userRepository.findByEmail(credentials);
			
			if(userinfo.isPresent()) {
					User getuserinfo =userinfo.get();
					
					if (!"JOB_SEEKER".equalsIgnoreCase(getuserinfo.getRole())) {
							throw new AccessDeniedException("Access denied for admin and recruiter");
					}
					
					if(jobApplicationRepository.findByUser_Id(getuserinfo.getId()).isEmpty()) {
						throw new ResourceNotFoundException("the user not applied any job yet");
					}
					
					return jobApplicationRepository.findByUser_Id(getuserinfo.getId());
			}else
				throw new AccessDeniedException("denied");								
	}
	
	
	
	public List<JobApplication> getApplicationByJob(Integer jobid){
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			
			String email = authentication.getName();
		
			User loggeduser=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not found"));
				
			Job job = jobRepository.findById(jobid).orElseThrow(()->new ResourceNotFoundException("Job Not Found"));
			
			if(!"RECRUITER".equalsIgnoreCase(loggeduser.getRole()) && !"ADMIN".equalsIgnoreCase(loggeduser.getRole())) {
				throw new AccessDeniedException("only admin and recruiter");
			}
			
			if("RECRUITER".equalsIgnoreCase(loggeduser.getRole()) && !job.getUser().getId().equals(loggeduser.getId())) {	
				throw new AccessDeniedException("you can access your own job application");
			}		
			
		return jobApplicationRepository.findByJob_Id(jobid);
	}
	
	
}
