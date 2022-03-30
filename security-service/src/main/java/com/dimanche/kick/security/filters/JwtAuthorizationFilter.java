package com.dimanche.kick.security.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import com.dimanche.kick.security.constante.ConstanteSecurity;
import com.dimanche.kick.security.util.SecurityUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private final SecurityUtil securityUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

		final String requestUrl = request.getRequestURI(); 
		
		if(requestUrl!=null && !requestUrl.equals(ConstanteSecurity.LOGIN_TOKEN_URL) && !requestUrl.equals(ConstanteSecurity.REFRESH_TOKEN_URL)) {
			securityUtil.verificationJWT(request,response);
	     }

        chain.doFilter(request, response);
    }


	
	
}
