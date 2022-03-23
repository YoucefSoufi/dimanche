package com.dimanche.kick.security.service.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.service.AccountService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
		
	private AccountService accountService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSprigSecurity());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.formLogin();
		http.authorizeRequests().anyRequest().authenticated();

	}
	

	public UserDetailsService userSprigSecurity() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				AppUser appUser = accountService.findUserByUserNameWithRoles(username);
				Collection<GrantedAuthority> autorization = new ArrayList<>(); 
				appUser.getAppRoles().forEach(n -> {autorization.add(new SimpleGrantedAuthority(n.getRoleName()));});
				return new User(appUser.getUserName(),"{bcrypt}"+appUser.getPassword(),autorization);
			}
		};
	}
}
