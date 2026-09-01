package com.lancer.HireHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancer.HireHub.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer>{

	List<JobApplication> findByUser_Id(Integer userId);
	
	List<JobApplication> findByJob_Id(Integer jobId);
	
	boolean existsByUser_IdAndJob_Id(Integer userid, Integer jobId);
	
	List<JobApplication> findByStatus(String status);
	
	Boolean existsByJob_Id(Integer jobId);
}
