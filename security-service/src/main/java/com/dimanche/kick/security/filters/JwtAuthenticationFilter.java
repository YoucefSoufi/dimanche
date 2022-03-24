package com.dimanche.kick.security.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dimanche.kick.security.constante.ConstanteSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
		
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String userName = request.getParameter(ConstanteSecurity.USER_NAME);
		String pw = request.getParameter(ConstanteSecurity.PASSWORD); 
		log.debug(userName+" "+pw);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, pw); 
		return authenticationManager.authenticate(authenticationToken);
	}
	

	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.debug("Yes you're IN Man **********************************************************************");
		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(ConstanteSecurity.MY_SECRET);
		String jwtAccessToken = JWT.create().
				withSubject(user.getUsername()).
				withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000)).
				withIssuer(request.getRequestURI().toString()).
				withClaim(ConstanteSecurity.ROLES, user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList())).sign(algorithm);
		response.setHeader(ConstanteSecurity.AUTHORIZATION, jwtAccessToken);
		String jwtRefreshToken = JWT.create().
				withSubject(user.getUsername()).
				withExpiresAt(new Date(System.currentTimeMillis()+1*60*60*1000)).
				withIssuer(request.getRequestURI().toString()).sign(algorithm);
		Map<String,String> idToken = new HashMap<>();
		
		idToken.put(ConstanteSecurity.JWT_ACCESS_TOKEN, jwtAccessToken);
		idToken.put(ConstanteSecurity.JWT_REFRESH_TOKEN, jwtRefreshToken);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), idToken);
		super.successfulAuthentication(request, response, chain, authResult);
	}

}
