package com.dimanche.kick.security.util;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.dimanche.kick.security.constante.ConstanteMessages;
import com.dimanche.kick.security.constante.ConstanteSecurity;
import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.form.JwtForm;
import com.dimanche.kick.security.service.AccountService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Component
public class SecurityUtil {
	
	private AccountService accountService;
	
	private MessagesSource messagesSource;
	
	public SecurityUtil(AccountService accountService, MessagesSource messagesSource) {
		super();
		this.accountService = accountService;
		this.messagesSource = messagesSource;
	}	
	
	Algorithm algo = Algorithm.HMAC256(ConstanteSecurity.MY_SECRET);
	
	JWTVerifier jwtVerifier = JWT.require(algo).build();
	
	public static void passwordEncoder(AppUser appUser) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
	}
	
    public void verificationJWT(HttpServletRequest request,HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
		final String requestTokenHeader = request.getHeader(ConstanteSecurity.AUTHORIZATION);
		if(StringUtils.hasLength(requestTokenHeader)) {
		    if (requestTokenHeader.startsWith(ConstanteSecurity.BEARER)) {
				String jwt = requestTokenHeader.substring(ConstanteSecurity.BEARER.length());
		        try {
		            String username = getUsernameFromToken(jwt);
		            if (StringUtils.hasLength(username) && null == SecurityContextHolder.getContext().getAuthentication()) {
		                UserDetails userDetails = accountService.loadUserByUsername(username);
		                if (validateToken(jwt, userDetails)) {
		                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
		                            new UsernamePasswordAuthenticationToken(
		                                    userDetails, null, userDetails.getAuthorities());
		                    usernamePasswordAuthenticationToken
		                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                    SecurityContextHolder.getContext()
		                            .setAuthentication(usernamePasswordAuthenticationToken);
		                }
		            }
		        }catch (Exception e) {
		        	log.error(e.getMessage());
		            httpException(response, e.getMessage());
		        }
		    } else {
		    	log.warn(messagesSource.getMessage(ConstanteMessages.AUCUN_TOKEN));
		    	String messageError = messagesSource.getMessage(ConstanteMessages.AUCUN_TOKEN);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            httpException(response, messageError);
		    }
		}else {
			String messageError = messagesSource.getMessage(ConstanteMessages.AUCUN_TOKEN);
			log.warn(messageError);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            new ObjectMapper().writeValue(response.getOutputStream(), messageError);
		}
	}

	private void httpException(HttpServletResponse response, String messageError)
			throws IOException, StreamWriteException, DatabindException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		new ObjectMapper().writeValue(response.getOutputStream(), messageError);
	}
	
	public JwtForm generateToken(UserDetails userDetails){

		JwtForm jwt = new JwtForm();
		Algorithm algorithm = Algorithm.HMAC256(ConstanteSecurity.MY_SECRET);
		
		String jwtAccessToken = JWT.create().
				withSubject(userDetails.getUsername()).
				withExpiresAt(new Date(System.currentTimeMillis()+ConstanteSecurity.DELAIS_ACCES_TOKEN)).
				withClaim(ConstanteSecurity.ROLES, userDetails.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList())).sign(algorithm);

		
		String jwtRefreshToken = JWT.create().
				withSubject(userDetails.getUsername()).
				withExpiresAt(new Date(System.currentTimeMillis()+ConstanteSecurity.DELAIS_REFRESH_TOKEN)).sign(algorithm);
		
		jwt.setAccesToken(jwtAccessToken);
		jwt.setRefreshToken(jwtRefreshToken);
		
		return jwt;
	}
	
	

    public String getUsernameFromToken(String token) {
		return jwtVerifier.verify(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return jwtVerifier.verify(token).getExpiresAt();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
