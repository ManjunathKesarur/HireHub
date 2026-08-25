package com.lancer.HireHub.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.lancer.HireHub.entity.User;
import com.lancer.HireHub.exception.EmailAlreadyExistException;
import com.lancer.HireHub.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email){
			
		Optional<User> user=	userRepository.findByEmail(email);
		
		if(user.isPresent()) {
			
			User ou=user.get();
			
		return org.springframework.security.core.userdetails.User.
				withUsername(ou.getEmail())
				.password(ou.getPassword())
				.roles(ou.getRole())
				.build();
		
		}else {
			throw new EmailAlreadyExistException("ACCESS DENIED");
		}
	}

}
