package com.lancer.HireHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancer.HireHub.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {

	
	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
 }
