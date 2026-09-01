package com.lancer.HireHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;
	
	public void mailMessage(String email,String company,String content) {
		
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		
		mailMessage.setFrom("shashidharkesarur@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setSubject(company);
		mailMessage.setText(content);
		
		javaMailSender.send(mailMessage);
		
	}
	
}
