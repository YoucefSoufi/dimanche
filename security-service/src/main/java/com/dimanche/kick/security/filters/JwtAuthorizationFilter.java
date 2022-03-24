package com.dimanche.kick.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dimanche.kick.security.constante.ConstanteMessages;
import com.dimanche.kick.security.constante.ConstanteSecurity;
import com.dimanche.kick.security.util.MessagesSource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private MessagesSource messagesSource;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug(messagesSource.getMessage(ConstanteMessages.START_METHODE_FILTRE));
		
		String bearerJwt = request.getHeader(ConstanteSecurity.AUTHORIZATION);

		if (bearerJwt != null && bearerJwt.startsWith(ConstanteSecurity.BEARER)) {

			verificationJWT(request, response, filterChain, bearerJwt);

		}else {
			filterChain.doFilter(request, response);
		}

	}

	private void verificationJWT(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
			String bearerJwt) throws IOException {
		try {

			String jwt = bearerJwt.substring(ConstanteSecurity.BEARER.length());

			Algorithm algo = Algorithm.HMAC256(ConstanteSecurity.MY_SECRET);

			JWTVerifier jwtVerifier = JWT.require(algo).build();

			DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

			String userName = decodedJWT.getSubject();

			List<String> roles = decodedJWT.getClaim(ConstanteSecurity.ROLES).asList(String.class);

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			roles.forEach(n -> {
				authorities.add(new SimpleGrantedAuthority(n));
			});

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userName, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			log.debug(messagesSource.getMessage(ConstanteMessages.END_METHODE_FILTRE));
			
			filterChain.doFilter(request, response);

		} catch (Exception e) {

			log.error(messagesSource.getMessage(ConstanteMessages.ERROR_JWT,request.getHeader(ConstanteSecurity.AUTHORIZATION)));
			log.error(e.getMessage());

			response.setHeader(messagesSource.getMessage(ConstanteMessages.ERROR),messagesSource.getMessage(ConstanteMessages.ERROR_JWT));

			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
}
