package com.lancer.HireHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancer.HireHub.entity.Job;

public interface JobRepository extends JpaRepository<Job,Integer>{

}
