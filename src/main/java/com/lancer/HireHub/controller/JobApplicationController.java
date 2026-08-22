package com.lancer.HireHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.entity.JobApplication;
import com.lancer.HireHub.service.JobApplicationService;

@RestController
@RequestMapping("/jobapplications")
public class JobApplicationController {


@Autowired
JobApplicationService applicationService;
	
	@PostMapping
	public JobApplication applyjob(@RequestBody	JobApplication jobApplication) {
		return applicationService.applyjob(jobApplication);
	}
	
	@GetMapping
	public List<JobApplication> getAllApplication(JobApplication application) {
		return applicationService.getAllApplication(application);
	}
}
