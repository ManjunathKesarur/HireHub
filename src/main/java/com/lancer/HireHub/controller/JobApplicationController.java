package com.lancer.HireHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.dto.JobApplicationDto;
import com.lancer.HireHub.entity.JobApplication;
import com.lancer.HireHub.service.JobApplicationService;

@RestController
@RequestMapping("/jobapplications")
public class JobApplicationController {


@Autowired
JobApplicationService applicationService;
	
	@PostMapping
	public JobApplication applyjob(@RequestBody	JobApplicationDto jobApplicationDto) {
		return applicationService.applyjob(jobApplicationDto);
	}
	
	@GetMapping
	public List<JobApplication> getAllApplication(
		@RequestParam(defaultValue = "0",required = false,value = "pageNumber")	Integer pageNumber,
			@RequestParam(defaultValue = "5",required = false,value = "pageSize")	Integer pageSize,
				@RequestParam(defaultValue = "job",required = false,value = "field")	String field){
		return applicationService.getAllApplication(pageNumber,pageSize,field);
	}
	
	@GetMapping("/{id}")
	public JobApplication getApplicationById(@PathVariable Integer id) {
	    return applicationService.getApplicationById(id);
	}
	
	@PatchMapping("/{id}")
	public JobApplication updateStatus(
	        @PathVariable Integer id,
	        @RequestParam String status) {

	    return applicationService.updateStatus(id, status);
	}
	
	@DeleteMapping("/{id}")
	public String deleteApplication(@PathVariable Integer id) {
	  return applicationService.deleteApplication(id);
	  
	}
	
	@GetMapping("/user")
	public List<JobApplication> getApplicationByUser() {
		return applicationService.getApplicationByUser();
	}
	
	
	@GetMapping("/job/{jobid}")
	public List<JobApplication> getApplicationByJob(@PathVariable	Integer jobid){
		return applicationService.getApplicationByJob(jobid);
	}
	
}
