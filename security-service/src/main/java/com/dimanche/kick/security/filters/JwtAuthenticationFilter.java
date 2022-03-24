package com.dimanche.kick.security.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
		
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String userName = request.getParameter("username");
		String pw = request.getParameter("password"); 
		log.debug(userName+" "+pw);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, pw); 
		return authenticationManager.authenticate(authenticationToken);
	}
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.debug("Yes you're IN Man **********************************************************************");
		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("mySecretKiri");
		String jwtAccessToken = JWT.create().
				withSubject(user.getUsername()).
				withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000)).
				withIssuer(request.getRequestURI().toString()).
				withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList())).sign(algorithm);
		response.setHeader("Authorization", jwtAccessToken);
		super.successfulAuthentication(request, response, chain, authResult);
	}

}
