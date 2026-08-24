package com.lancer.HireHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lancer.HireHub.entity.Job;

public interface JobRepository extends JpaRepository<Job,Integer>{

	List<Job> findByTitleContainingIgnoreCase(String title);
	
	Optional<Job> findByCompanyAndTitleContainingIgnoreCase(String company,String title);
	
	List<Job> findByStatusIgnoreCase(String status);
	
	List<Job> findByLocationIgnoreCase(String location);
	
	List<Job> findBySalaryGreaterThanEqual(Double salary);
	
	List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(String title,String location);

}