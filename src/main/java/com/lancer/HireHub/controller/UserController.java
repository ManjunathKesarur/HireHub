package com.lancer.HireHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancer.HireHub.dto.UserDto;
import com.lancer.HireHub.entity.User;
import com.lancer.HireHub.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

	@Autowired
	UserService userService;
	
	
	@PostMapping
	public ResponseEntity<String> saveUser(@Valid	 @RequestBody UserDto userDto) {
	 
	return ResponseEntity.ok(userService.saveUser(userDto));
	}
	
	@GetMapping
	public List<User> getAllUsers(
			@RequestParam(defaultValue = "0",required = false,value = "pageNumber")	Integer pageNumber,
				@RequestParam(defaultValue = "5",required = false,value = "pageSize")	Integer pageSize,
					@RequestParam(defaultValue = "id",required = false,value = "field")	String field){
		return userService.getAllUsers(pageNumber, pageSize, field);
	}
	
	@GetMapping("{id}")
	public User getUserById(@PathVariable int id) {
		return userService.getUserById(id);
	}
	
	@PatchMapping("{pid}")
	public User updateUser(	@PathVariable int pid,@RequestBody User user ) {
		return userService.updateUser(pid, user);
	}
	
	@DeleteMapping("{id}")
	public String deleteById(@PathVariable int id) {
		return userService.deleteById(id);
	}
	
	}
