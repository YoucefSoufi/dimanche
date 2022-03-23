package com.dimanche.kick.security.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dimanche.kick.security.entites.AppRole;
import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.exception.BusinessException;
import com.dimanche.kick.security.repository.AppRoleRepository;
import com.dimanche.kick.security.repository.AppUserRepository;
import com.dimanche.kick.security.service.AccountService;
import com.dimanche.kick.security.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Transactional
@Data
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private SecurityUtil securityUtil;


	@Override
	public AppUser addNewUser(AppUser appUser) {
		securityUtil.passwordEncoder(appUser);
		return appUserRepository.save(appUser);
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
//		AppRole appRole = appRoleRepository.findByRoleName(roleName);
//		AppUser appUser = appUserRepository.findByUserName(userName);
//		appUser.getAppRoles().add(appRole);
		
	}

	@Override
	public AppUser loadUserByUserName(String userName) throws BusinessException {
		return appUserRepository.findByUserName(userName).orElseThrow(()-> new BusinessException("Aucun utilisateur avec l'identifiant "+userName));
	}

	@Override
	public List<AppUser> users() {
		return appUserRepository.findAll();
	}

	@Override
	public List<AppUser> addNewUsers(List<AppUser> appUser) {
		appUser.forEach(n -> {securityUtil.passwordEncoder(n);});
		return appUserRepository.saveAll(appUser);
	}

	@Override
	public List<AppUser> usersWithRoles() {
		return appUserRepository.findAllWithRoles();
	}

	@Override
	public AppUser addRolesToUser(String appRoleName, String appUserName) throws BusinessException {
		AppUser appUser = appUserRepository.findByUserName(appUserName).orElseThrow(()-> new BusinessException("L'utilisateur avec l'identifiant "+appUserName+" n'existe pas"));
		AppRole appRole = appRoleRepository.findByRoleName(appRoleName).orElseThrow(() -> new BusinessException("Le Role "+appRoleName+" n'existe pas"));
		appUser.getAppRoles().add(appRole);
		return appUser;
	}

	@Override
	public AppUser findUserByUserNameWithRoles(String userName) {
		return appUserRepository.findByUserNameWithRoles(userName).get();
	}

}
