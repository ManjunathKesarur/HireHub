package com.lancer.HireHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.dto.JobClosingDto;
import com.lancer.HireHub.dto.JobDto;
import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	JobService jobService;
	
	
	@PostMapping
	public Job svaeJob(@Valid	@RequestBody JobDto jobDto) {
		return jobService.svaeJob(jobDto);
	}
	
	
	@GetMapping
	public List<Job> getAllJobs(
		@RequestParam(defaultValue = "0",required = false,value = "pageNumber")	Integer pageNumber,
			@RequestParam(defaultValue = "5",required = false,value = "pageSize")	Integer pageSize,
				@RequestParam(defaultValue = "title",required = false,value = "field")	String field){
		
		return jobService.getAllJobs(pageNumber, pageSize, field);
	}
	
	
	@GetMapping("/{id}")
	public Job getJobById(@PathVariable	Integer id) {
		return jobService.getJobById(id);
	}
	
	
	@PutMapping("/{id}")
	public String updateJob(@PathVariable(required = true) Integer id,@RequestBody Job job) {
		return jobService.updateJob(id, job);
	}
	
	
	@DeleteMapping("/{id}")
	public String deleteJob(@PathVariable	Integer id) {
		return jobService.deleteJob(id);
	}
	
	
	@GetMapping("/title")
	public List<Job> searchJobs(@RequestParam String title) {
		return jobService.searchJobs(title);
	}
	
	
	@PatchMapping("/closing")
	public Job jobStatusClosing(@Valid   @RequestBody	JobClosingDto jobDto) {
		return jobService.jobStatusClosing(jobDto);
	}
	
	@GetMapping("/status/open")
	public List<Job> getOpenJobs(){
		return jobService.getOpenJobs();
	}
	
	@GetMapping("/location/{location}")
	public List<Job> getJobByLocation(@PathVariable String location ){
		return jobService.getJobByLocation(location);
	}
	
	@GetMapping("/salary/{salary}")
	public List<Job> getJobBySalary(@PathVariable	Double salary){
		return jobService.getJobBySalary(salary);
	}
	
	@GetMapping("/title/location")
	public List<Job> getJobByTitleAndLocation(@RequestParam String title,@RequestParam String location){
		return jobService.getJobByTitleAndLocation(title,location);
	}
	
	@GetMapping("/title/company")
	public List<Job> getJobByTitleAndCompany(@RequestParam	String title,@RequestParam	String company){
		return jobService.getJobByTitleAndCompany(title, company);
	}
	
	@GetMapping("/jobtype")
	public List<Job> getJobByJobType(@RequestParam	String jobtype){
		return	jobService.getJobByJobType(jobtype);
	}
	
	@GetMapping("user_id/{user_id}")
	public List<Job> getJobByUser_Id(@PathVariable	Integer user_id){
		return jobService.getJobByUser_Id(user_id);
	}
	
}
