package com.lancer.HireHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.entity.Job;
import com.lancer.HireHub.repository.JobRepository;

@Service
public class JobSerice {

	@Autowired
	JobRepository jobRepository;
	
	public String svaeJob(Job job) {
		jobRepository.save(job);
		return "Job Added";
	}
}
