package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.entity.User;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public User getUserById(int id) {
	Optional<User> ou=	userRepository.findById(id);
			User u=ou.get();
			return u;
	}
	
	public User updateUser(int id,User user) {
	Optional<User> ooe	=userRepository.findById(id);
		User dbuser=ooe.get();
	if(dbuser != null) {
		dbuser.setName(user.getName());
		dbuser.setEmail(user.getEmail());
		dbuser.setPassword(user.getPassword());
		dbuser.setPhone(user.getPhone());
		dbuser.setRole(user.getRole());
	return userRepository.save(dbuser);
	}
	else {
		return null;
	}
	}
	
	public String deleteById(int id) {
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return "data deleted successfully";
		}else {
			return "data not found to delete";
		}
	}
}
