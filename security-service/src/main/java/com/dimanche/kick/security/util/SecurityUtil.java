package com.dimanche.kick.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.dimanche.kick.security.entites.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@Data
@AllArgsConstructor
public class SecurityUtil {
	
	public void passwordEncoder(AppUser appUser) {
		PasswordEncoder encode = new BCryptPasswordEncoder();
		appUser.setPassword(encode.encode(appUser.getPassword()));
	}

}