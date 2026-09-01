package com.lancer.HireHub.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

@Bean	
public 	SecurityFilterChain filterChain(HttpSecurity http,
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
	
	http.		
				 csrf(csrf->csrf.disable())
				 .authenticationProvider(provider(userDetailsService, passwordEncoder))
				 .authorizeHttpRequests(auth->auth
						 
						 .requestMatchers(HttpMethod.GET, "/jobs/**")
						 .permitAll()
						 
						 .requestMatchers(HttpMethod.PATCH, "/jobs/**")
						 .hasAnyRole("ADMIN", "RECRUITER")
						 
						 .requestMatchers(HttpMethod.POST, "/jobs/**")
						 .hasAnyRole("ADMIN", "RECRUITER")

						 .requestMatchers(HttpMethod.PUT, "/jobs/**")
						 .hasAnyRole("ADMIN", "RECRUITER")

						 .requestMatchers(HttpMethod.DELETE, "/jobs/**")
						 .hasAnyRole("ADMIN", "RECRUITER")
						 
						 
						 
						 .requestMatchers(HttpMethod.GET,"/jobapplications")
						 .hasRole("ADMIN")
						 
						 .requestMatchers(HttpMethod.GET, "/jobapplications/**")
						 .hasAnyRole("JOB_SEEKER", "RECRUITER", "ADMIN")

						 .requestMatchers(HttpMethod.POST, "/jobapplications")
						 .hasRole("JOB_SEEKER")

						 .requestMatchers(HttpMethod.PATCH, "/jobapplications/**")
						 .hasAnyRole("RECRUITER", "ADMIN")

						 .requestMatchers(HttpMethod.DELETE, "/jobapplications/**")
						 .hasAnyRole("RECRUITER", "ADMIN")
						 
						 
						 
						 .requestMatchers(HttpMethod.POST, "/users")
						 .permitAll()
						 
						 .requestMatchers(HttpMethod.GET,"/users/*")
						 .hasAnyRole("JOB_SEEKER", "RECRUITER", "ADMIN")
						 
						 .requestMatchers(HttpMethod.PATCH,"/users/**")
						 .hasAnyRole("JOB_SEEKER", "RECRUITER", "ADMIN")
						 
						 .requestMatchers(HttpMethod.DELETE,"/users/**")
						 .hasAnyRole("JOB_SEEKER", "RECRUITER", "ADMIN")
						 
						 .requestMatchers("/users/**")
						 .hasRole("ADMIN")
						 
						 .anyRequest().authenticated())	
					
				 	.httpBasic(Customizer.withDefaults());
	
	return http.build();
	
}

@Bean
public	AuthenticationProvider provider(UserDetailsService detailsService,PasswordEncoder encoder) {
	DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(detailsService);
	daoAuthenticationProvider.setPasswordEncoder(encoder);
	
	return daoAuthenticationProvider;
}
}
