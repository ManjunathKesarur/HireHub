package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.dto.JobClosingDto;
import com.lancer.HireHub.dto.JobDto;
import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.exception.EmailAlreadyExistException;
import com.lancer.HireHub.repository.JobRepository;

@Service
public class JobService {

	@Autowired
	JobRepository jobRepository;
	
	public String svaeJob(JobDto jobDto) {
		Job job = new Job();

	    job.setTitle(jobDto.getTitle());
	    job.setDescription(jobDto.getDescription());
	    job.setLocation(jobDto.getLocation());
	    job.setCompany(jobDto.getCompany());
	    job.setSalary(jobDto.getSalary());
	    job.setJobType(jobDto.getJobType());
	    job.setStatus("OPEN");

	     jobRepository.save(job);
	    
	    return "data inserted";
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
	
	
	public String updateJob(Integer id, Job job) {
	Optional<Job> optional=jobRepository.findById(id);
	if(optional.isPresent()) {
		
		Job existing=optional.get();
		
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
		}else
				throw new EmailAlreadyExistException("the entered id is wrong");
	}
	
	
	public String deleteJob(Integer id) {
		
			if(jobRepository.existsById(id)) {
				jobRepository.deleteById(id);
					return "data deleted";
			}else {
				throw new EmailAlreadyExistException("no data to delete according to id");
			}
	}
	
	
	public List<Job> searchJobs(String title) {
		
		List<Job> lists=jobRepository.findByTitleContainingIgnoreCase(title);
		if(lists.isEmpty()) {
			throw new EmailAlreadyExistException("entered "+title+" is not present");
		}else {
			return lists;
		}	
	}
	
	public Job jobStatusClosing(JobClosingDto jobDto) {
		Optional<Job>	op=	jobRepository.findByCompanyAndTitleContainingIgnoreCase(jobDto.getCompany(),jobDto.getTitle());
		if(op.isPresent()) {
			
			Job jobs=op.get();
			
			if(jobs.getStatus().equalsIgnoreCase("CLOSED"))
				throw new EmailAlreadyExistException("Job Is Already Closed");       /// will change later	
			
			jobs.setStatus("CLOSED");
			return	jobRepository.save(jobs);
			
			
		}else {
			throw new EmailAlreadyExistException("not job posting from the "+jobDto.getCompany()+" with the title "+jobDto.getTitle());
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
