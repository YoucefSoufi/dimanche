package com.dimanche.kick.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
@EnableWebMvc
public class ExceptionHelper extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = { AccessDeniedException.class })
	public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex) {
		log.error("Exception : "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = { IllegalArgumentException.class })
	public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex) {
		log.error("Exception : "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(value = { InvalidInputException.class })
	public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {

		log.error("Invalid Input Exception : "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { Unauthorized.class })
	public ResponseEntity<Object> handleUnauthorizedException(Unauthorized ex) {
		log.error("Unauthorized Exception : "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { BusinessException.class })
	public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
		log.error("Business Exception : "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("Exception: "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> runtimeException(RuntimeException ex) {
		log.error("Exception: "+ ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	


}
