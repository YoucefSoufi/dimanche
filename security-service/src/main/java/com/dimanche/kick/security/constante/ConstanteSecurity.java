package com.dimanche.kick.security.constante;

public interface ConstanteSecurity {

	static final String AUTHORIZATION ="Authorization";

	static final String JWT_ACCESS_TOKEN ="jwtAccessToken";

	static final String JWT_REFRESH_TOKEN ="jwtRefreshToken";

	static final String MY_SECRET = "mySecretKiri";

	static final String USER_NAME = "username";

	static final String PASSWORD = "password";

	static final String ROLES = "roles";
	
	static final String BEARER = "Bearer "; /**Attention il ne faut pas supprimer l'espace après Bearer **/
	
}
