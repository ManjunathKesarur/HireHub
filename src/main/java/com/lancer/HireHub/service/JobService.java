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

import com.lancer.HireHub.dto.JobClosingDto;
import com.lancer.HireHub.dto.JobDto;
import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.entity.User;
import com.lancer.HireHub.exception.EmailAlreadyExistException;
import com.lancer.HireHub.repository.JobApplicationRepository;
import com.lancer.HireHub.repository.JobRepository;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class JobService {

	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JobApplicationRepository jobApplicationRepository;
	
	public Job svaeJob(JobDto jobDto) {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();	
		
		String email=authentication.getName();
		
		Optional<User> optional=	userRepository.findByEmail(email);
		
		if(optional.isPresent()) {
			
			
			User user=	optional.get();
			
		if(!user.getRole().equalsIgnoreCase("RECRUITER"))
			throw new EmailAlreadyExistException("Only Recruiter Can Create Jobs");
			
		if(jobRepository.existsByCompanyAndTitleAndUser(jobDto.getCompany(),jobDto.getTitle(),user)) {	
			throw new EmailAlreadyExistException("Company exists");
		}
			
		Job job = new Job();

	    job.setTitle(jobDto.getTitle());
	    job.setDescription(jobDto.getDescription());
	    job.setLocation(jobDto.getLocation());
	    job.setCompany(jobDto.getCompany());
	    job.setSalary(jobDto.getSalary());
	    job.setJobType(jobDto.getJobType());
	    job.setStatus("OPEN");
	    job.setUser(user);

	    return    jobRepository.save(job);
	    
		}else {
			throw new EmailAlreadyExistException("Logged-in user not found");
		}
	}
	
		
	
	public String updateJob(Integer id, Job job) {
	
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		User loggedUser = userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException("user not found"));
		
		Job existing=jobRepository.findById(id).orElseThrow(()->new EmailAlreadyExistException("entered id user not found"));
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			
			if(job.getTitle()!=null)
				existing.setTitle(job.getTitle());
			
			if(job.getDescription()!=null)
				existing.setDescription(job.getDescription());
			
			if(job.getCompany()!=null)
				existing.setCompany(job.getCompany());
			
			if(job.getJobType()!=null)
				existing.setJobType(job.getJobType());
			
			if(job.getLocation()!=null)
				existing.setLocation(job.getLocation());
			
			if(job.getSalary()!=null)
				existing.setSalary(job.getSalary());
			
			jobRepository.save(existing);
			
					return "data updated";
		}
		if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(existing.getUser()==null || !existing.getUser().getId().equals(loggedUser.getId()) ) {
				throw new EmailAlreadyExistException("you cant modify other's application");
			}
			
			if(job.getTitle()!=null)
				existing.setTitle(job.getTitle());
			
			if(job.getDescription()!=null)
				existing.setDescription(job.getDescription());
			
			if(job.getCompany()!=null)
				existing.setCompany(job.getCompany());
			
			if(job.getJobType()!=null)
				existing.setJobType(job.getJobType());
			
			if(job.getLocation()!=null)
				existing.setLocation(job.getLocation());
			
			if(job.getSalary()!=null)
				existing.setSalary(job.getSalary());
			
			jobRepository.save(existing);
			
					return "data updated";
					
		}
		
		
		throw new EmailAlreadyExistException("Access denied");
	}
	
	
	public String deleteJob(Integer id) {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		User loggedUser = userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException("user not found"));
		
		Job existing=jobRepository.findById(id).orElseThrow(()->new EmailAlreadyExistException("entered id user not found"));
		
		if(jobApplicationRepository.existsByJob_Id(id)) {
			  throw new EmailAlreadyExistException("Cannot delete this job because applications exist. Close the job instead.");
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			jobRepository.deleteById(id);
			return "Data Deleted";
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(existing.getUser()==null || !existing.getUser().getId().equals(loggedUser.getId()) ) {
				throw new EmailAlreadyExistException("You cannot delete another recruiter's job");
			}
			
			jobRepository.deleteById(id);
			return "Data Deleted";
		}
		
		throw new EmailAlreadyExistException("Access denied");
	}
	

	
	public Job jobStatusClosing(JobClosingDto jobDto) {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		User loggedUser = userRepository.findByEmail(email).orElseThrow(()->new EmailAlreadyExistException("user not found"));
		
		Job existing=jobRepository.findByCompanyAndTitleContainingIgnoreCase(jobDto.getCompany(),jobDto.getTitle()).orElseThrow(()->new EmailAlreadyExistException("entered id user not found"));
		
		
		if(existing.getStatus().equalsIgnoreCase("CLOSED"))
			throw new EmailAlreadyExistException("Job Is Already Closed");  
		
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			existing.setStatus("CLOSED");
			return jobRepository.save(existing);
		}
		
		
		if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(existing.getUser()==null || !existing.getUser().getId().equals(loggedUser.getId()) ) {
				throw new EmailAlreadyExistException("You cannot close another recruiter's job");
			}
			
			existing.setStatus("CLOSED");
			return jobRepository.save(existing);
		}
		
		throw new EmailAlreadyExistException("Access denied");
    }

	
	
	public List<Job> getJobByUser_Id(Integer user_id) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    User loggedUser = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new EmailAlreadyExistException("User not found"));

	    if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) {

	        return jobRepository.findByUser_Id(user_id);
	    }
	    if (loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {

	        if (!loggedUser.getId().equals(user_id)) {

	            throw new EmailAlreadyExistException(
	                    "You can view only your own jobs");
	        }

	        return jobRepository.findByUser_Id(user_id);
	    }

	    throw new EmailAlreadyExistException("Access denied");
	}
	
	
	
	public List<Job> getAllJobs(Integer pageNumber,Integer pageSize,String field){
		Sort sort=Sort.by(field).ascending();
		Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
		Page<Job> page	=jobRepository.findAll(pageable);
		if(page.isEmpty()) {
			throw new EmailAlreadyExistException("no jobs are found");			/// temprovary i will update later
			}
				return page.getContent();
	}
	
	
	public Job getJobById(Integer id) {
		Optional<Job> optional= jobRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			throw new EmailAlreadyExistException("no data found on the given id");   //same as above
	}
	
	
	
	public List<Job> searchJobs(String title) {
		
		List<Job> lists=jobRepository.findByTitleContainingIgnoreCase(title);
		if(lists.isEmpty()) {
			throw new EmailAlreadyExistException("entered "+title+" is not present");
		}else {
			return lists;
		}	
	}
	
	
	
	public List<Job> getOpenJobs(){
		return jobRepository.findByStatusIgnoreCase("OPEN");
	}
	
	public List<Job> getJobByLocation(String location){
		return jobRepository.findByLocationIgnoreCase(location);
	}
	
	public List<Job> getJobBySalary(Double salary){
		return jobRepository.findBySalaryGreaterThanEqual(salary);
	}
	
	public List<Job> getJobByTitleAndLocation(String title,String location){
		return jobRepository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(title, location);
	}
	
	public List<Job> getJobByTitleAndCompany(String title,String company){
		return jobRepository.findByTitleContainingIgnoreCaseAndCompanyContainingIgnoreCase(title,company);
	}
	
	public List<Job> getJobByJobType(String jobType){
		return jobRepository.findByJobTypeContainingIgnoreCase(jobType);
	}
	
}
