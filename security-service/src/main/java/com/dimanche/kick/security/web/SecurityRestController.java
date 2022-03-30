package com.dimanche.kick.security.web;

import java.io.IOException;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dimanche.kick.security.constante.ConstanteMessages;
import com.dimanche.kick.security.constante.ConstanteSecurity;
import com.dimanche.kick.security.exception.BusinessException;
import com.dimanche.kick.security.form.JwtForm;
import com.dimanche.kick.security.service.AccountService;
import com.dimanche.kick.security.util.MessagesSource;
import com.dimanche.kick.security.util.SecurityUtil;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/auth")
public class SecurityRestController {
	
	private   final MessagesSource messagesSource;
	private   final AccountService appAccountService;
	private   final AuthenticationManager authenticationManager;
	private	  final SecurityUtil securityUtil;
	
	
	@PostMapping("/login")
    public ResponseEntity<JwtForm> loginUser(@RequestParam("userName") String username,
                                       @RequestParam("password") String password) throws BusinessException {
		
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username
                    , password));
            if (auth.isAuthenticated()) {
                log.info("Logged In");
                UserDetails userDetails = appAccountService.loadUserByUsername(username);
                JwtForm token = securityUtil.generateToken(userDetails);
                return new ResponseEntity<>(token,HttpStatus.ACCEPTED);
            } else {
    			throw new AccessDeniedException(messagesSource.getMessage(ConstanteMessages.ACCES_NON_AUTORISE));
            }
    }
	
	@GetMapping("/refreshToken")
	public ResponseEntity<JwtForm> refreshToken(HttpServletRequest request, HttpServletResponse response) throws BusinessException, StreamWriteException, DatabindException, IOException{
		
		String bearerJwt = request.getHeader(ConstanteSecurity.AUTHORIZATION);
		
		JwtForm token = new JwtForm();
		
		if (bearerJwt != null && bearerJwt.startsWith(ConstanteSecurity.BEARER)) {
			
			String jwt = bearerJwt.substring(ConstanteSecurity.BEARER.length());
			
			String user = securityUtil.getUsernameFromToken(jwt);
			
			UserDetails userDetails = appAccountService.loadUserByUsername(user);


			verificationBlackList(userDetails);
			
			token =securityUtil.generateToken(userDetails);
			
			return new ResponseEntity<>(token,HttpStatus.ACCEPTED);

		}else {
			throw new BusinessException(messagesSource.getMessage(ConstanteMessages.JWT_OBLIGATOIRE));
		}
	}


	private void verificationBlackList(UserDetails userDetailsr) throws BusinessException {
		Random random = new Random();
		if(random.nextBoolean()) {
			log.debug("Utilisateur "+userDetailsr.toString()+"OK");
		}else {
			
		}
		
	}
	

}
