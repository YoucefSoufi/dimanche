package com.dimanche.kick.security.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtForm {
	
	private String accesToken;
	private String refreshToken;
	
	

}
