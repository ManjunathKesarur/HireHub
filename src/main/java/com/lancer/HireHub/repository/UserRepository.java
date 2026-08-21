package com.lancer.HireHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancer.HireHub.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {

}
