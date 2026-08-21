package com.lancer.HireHub.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
		
		Map<String, String> map=new HashMap<String, String>();
			
		List<FieldError> fields=exception.getBindingResult().getFieldErrors();
		
		for (FieldError fieldError : fields) {
			map.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return new ResponseEntity<Map<String, String>>(map,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<String> emailAlreadyExistsException(EmailAlreadyExistException alreadyExists) {
		return new ResponseEntity<String>(alreadyExists.getMessage(),HttpStatus.CONFLICT);
	}
	
}
