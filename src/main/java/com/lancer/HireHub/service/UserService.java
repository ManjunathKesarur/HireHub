package com.lancer.HireHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.dto.UserDto;
import com.lancer.HireHub.entity.User;
import com.lancer.HireHub.exception.EmailAlreadyExistException;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	public String saveUser(UserDto userDto) {
		
		if(userRepository.existsByEmail(userDto.getEmail())) {
			throw new EmailAlreadyExistException("Email Already exists enter unique email");
		}
		if(!userDto.getRole().equalsIgnoreCase("JOB_SEEKER") && !userDto.getRole().equalsIgnoreCase("RECRUITER")) {
			throw new EmailAlreadyExistException("only JOB_SEEKER  and  RECRUITER  can register");
		}
		
			User user=new User();
			user.setName(userDto.getName());
			user.setEmail(userDto.getEmail());
			user.setPassword(encoder.encode(userDto.getPassword()));
			user.setPhonenumber(userDto.getPhonenumber());
			user.setRole(userDto.getRole());
			
		 userRepository.save(user);
		 return "User Registerd";
	}
	
	
	public List<User> getAllUsers(Integer pageNumber,Integer pageSize,String field){
		Sort sortz=Sort.by(field).ascending();
		Pageable pageable=PageRequest.of(pageNumber,pageSize,sortz);
		Page<User> e=	userRepository.findAll(pageable);
		
		if(e.isEmpty())
			throw new EmailAlreadyExistException("No Records Found");
			
			return 	e.getContent();
	}
	
	
	public User getUserById(int id) {
	
		Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		User loggedUser =userRepository.findByEmail(email).orElseThrow(()-> new EmailAlreadyExistException("the user not found"));
	
		User user=userRepository.findById(id).orElseThrow(()->new EmailAlreadyExistException("the user not found"));
		
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			return user; 
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(!loggedUser.getId().equals(user.getId())) {
				throw new EmailAlreadyExistException("you can't access others details as you are a :"+loggedUser.getRole());
			}
			return user;
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER")) {
			if(!loggedUser.getId().equals(user.getId())) {
				throw new EmailAlreadyExistException("you can't access others details as you are a :"+loggedUser.getRole());
			}
			return user;
		}
		
		throw new EmailAlreadyExistException("access denied");
	}
	
	
	
	public User updateUser(int id, User user) {
		
	    Optional<User> dbusers = userRepository.findById(id);
	    	
	    if(dbusers.isPresent()) {
	 	
	    	User dbuser = dbusers.get();
	    if (dbuser != null) {

	        if (user.getName() != null)
	            dbuser.setName(user.getName());

	        if (user.getEmail() != null)
	            dbuser.setEmail(user.getEmail());

	        if (user.getPassword() != null)
	            dbuser.setPassword(encoder.encode(user.getPassword()));

	        if (user.getPhonenumber() != null)
	            dbuser.setPhonenumber(user.getPhonenumber());

	        if (user.getRole() != null)
	            dbuser.setRole(user.getRole());

	        return userRepository.save(dbuser);
	    }
	    }
	    throw new EmailAlreadyExistException("No data found on id : "+id+" to update the record");      //will change later
	}
	
	
	public String deleteById(int id) {
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return "data deleted successfully";
		}else {
			return "data not found to delete";
		}
	}
	
	public String login(String email,String password) {
		Optional<User> optional	=userRepository.findByEmail(email);
			
			if(optional.isPresent()) {
				User user=optional.get();
				if(encoder.matches(password, user.getPassword())) {
				    return "logined";
				}
				return "incorret password";
				}
			else {
			return "invalid email";
			}
	}
}
