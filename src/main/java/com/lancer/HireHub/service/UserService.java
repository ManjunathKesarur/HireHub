package com.lancer.HireHub.service;

import java.util.List;

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
import com.lancer.HireHub.exception.AccessDeniedException;
import com.lancer.HireHub.exception.AlreadyExistsException;
import com.lancer.HireHub.exception.ResourceNotFoundException;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	public String saveUser(UserDto userDto) {
		
		if(userRepository.existsByEmail(userDto.getEmail())) {
			throw new AlreadyExistsException("Email Already exists enter unique email");
		}
		if(!userDto.getRole().equalsIgnoreCase("JOB_SEEKER") && !userDto.getRole().equalsIgnoreCase("RECRUITER")) {
			throw new AccessDeniedException("only JOB_SEEKER  and  RECRUITER  can register");
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
			throw new ResourceNotFoundException("No Records Found");
			
			return 	e.getContent();
	}
	
	
	public User getUserById(int id) {
	
		Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		User loggedUser =userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("the user not found"));
	
		User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("the user not found"));
		
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			return user; 
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(!loggedUser.getId().equals(user.getId())) {
				throw new AccessDeniedException("you can't access others details as you are a :"+loggedUser.getRole());
			}
			return user;
		}
		
		if(loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER")) {
			if(!loggedUser.getId().equals(user.getId())) {
				throw new AccessDeniedException("you can't access others details as you are a :"+loggedUser.getRole());
			}
			return user;
		}
		
		throw new AccessDeniedException("access denied");
	}
	
	
	
	public User updateUser(int id, User user) {
		
		
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		User loggedUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("user not found"));
		
		User dbuser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("the entered id user not found"));
		
		if(loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
			if(dbuser != null) {
				
				if(user.getName() != null)
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
		
		if(loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER") || loggedUser.getRole().equalsIgnoreCase("RECRUITER")) {
			
			if(!loggedUser.getId().equals(dbuser.getId())) {
				throw new AccessDeniedException("you cant modify the other's job-seeker/recruiter data");
			
			}
				
				if(user.getRole() == null) { 
			
				if(user.getName() != null)
					dbuser.setName(user.getName());
				
		        if (user.getEmail() != null)
		            dbuser.setEmail(user.getEmail());

		        if (user.getPassword() != null)
		            dbuser.setPassword(encoder.encode(user.getPassword()));

		        if (user.getPhonenumber() != null)
		            dbuser.setPhonenumber(user.getPhonenumber());

		        if (user.getRole() != null)
		            dbuser.setRole(loggedUser.getRole());

		        return userRepository.save(dbuser);
			}else {
				throw new AccessDeniedException("ONLY ADMIN CAN CHANGE THE ROLE");
			}
		}
	
	    throw new AccessDeniedException("AccessDenied");     
	}
	
	
	public String deleteById(int id) {

		    Authentication authentication =
		            SecurityContextHolder.getContext().getAuthentication();

		    String email = authentication.getName();

		    User loggedUser = userRepository.findByEmail(email)
		            .orElseThrow(() ->
		                    new ResourceNotFoundException("User not found"));

		    User userToDelete = userRepository.findById(id)
		            .orElseThrow(() ->
		                    new ResourceNotFoundException("User not found"));


		    if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) {

		        userRepository.delete(userToDelete);

		        return "User deleted successfully";
		    }

		
		    if (loggedUser.getRole().equalsIgnoreCase("RECRUITER")
		            || loggedUser.getRole().equalsIgnoreCase("JOB_SEEKER")) {

		        if (!loggedUser.getId().equals(userToDelete.getId())) {
		            throw new AccessDeniedException(
		                    "You cannot delete another user's account");
		        }

		        userRepository.delete(userToDelete);

		        return "User deleted successfully";
		    }

		    throw new AccessDeniedException("Access denied");
		}
	
}
