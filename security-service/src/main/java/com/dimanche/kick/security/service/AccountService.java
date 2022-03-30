package com.dimanche.kick.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dimanche.kick.security.entites.AppRole;
import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.exception.BusinessException;

public interface AccountService extends  UserDetailsService {

	AppUser addNewUser(AppUser appUser);
	
	AppRole addNewRole(AppRole appRole);
	
	void addRoleToUser(String userName, String roleName);
	
	AppUser loadUserByUserName(String userName) throws BusinessException;
	
	List<AppUser> users();
		
	List<AppUser> addNewUsers(List<AppUser> appUser);
	
	List<AppUser> usersWithRoles();
	
	AppUser addRolesToUser(String appRoleName, String appUserName) throws BusinessException;
	
	AppUser findUserByUserNameWithRoles(String userName);
}
