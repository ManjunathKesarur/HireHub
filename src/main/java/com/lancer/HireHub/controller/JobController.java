package com.lancer.HireHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.service.JobSerice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	JobSerice jobSerice;
	
	
	@PostMapping
	public String svaeJob(@Valid	@RequestBody Job job) {
		return jobSerice.svaeJob(job);
	}
	
	
	@GetMapping
	public List<Job> getAllJobs(
		@RequestParam(defaultValue = "0",required = false,value = "pageNumber")	Integer pageNumber,
			@RequestParam(defaultValue = "5",required = false,value = "pageSize")	Integer pageSize,
				@RequestParam(defaultValue = "title",required = false,value = "field")	String field){
		
		return jobSerice.getAllJobs(pageNumber, pageSize, field);
	}
	
	
	@GetMapping("/{id}")
	public Job getJobById(@PathVariable	Integer id) {
		return jobSerice.getJobById(id);
	}
	
	
	@PutMapping("/{id}")
	public String updateJob(@PathVariable(required = true) Integer id,@RequestBody Job job) {
		return jobSerice.updateJob(id, job);
	}
	
	
	@DeleteMapping("/{id}")
	public String deleteJob(@PathVariable	Integer id) {
		return jobSerice.deleteJob(id);
	}
	
	
	@GetMapping("/search")
	public List<Job> searchJobs(@RequestParam String title) {
		return jobSerice.searchJobs(title);
	}
}
