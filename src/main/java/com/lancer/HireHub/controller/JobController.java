package com.lancer.HireHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.service.JobSerice;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	JobSerice jobSerice;
	
	
	@PostMapping
	public String svaeJob(@RequestBody	Job job) {
		return jobSerice.svaeJob(job);
	}
}
