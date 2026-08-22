package com.lancer.HireHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancer.HireHub.entity.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer>{

	List<JobApplication> findByUserid(Integer userid);
}
