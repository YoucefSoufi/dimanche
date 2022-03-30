package com.dimanche.kick.security.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.exception.BusinessException;
import com.dimanche.kick.security.form.RoleUserForm;
import com.dimanche.kick.security.service.AccountService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AccountRestController {

	private AccountService appAccountService;
	
	@GetMapping("/users")
	public ResponseEntity<List<AppUser>> appUsers(){
		return new ResponseEntity<>(appAccountService.users(),HttpStatus.OK);
	}
	
	@PostMapping("/users")
	@PostAuthorize("hasAuthority('Admin')")
	public ResponseEntity<AppUser> addAppUsers(@RequestBody AppUser appUser){
		return new ResponseEntity<>(appAccountService.addNewUser(appUser),HttpStatus.CREATED);
	}
	
	@PostMapping("/users/roles")
	public ResponseEntity<AppUser> addAppUsers(@RequestBody RoleUserForm roleUserForm) throws BusinessException{
		return new ResponseEntity<>(appAccountService.addRolesToUser(roleUserForm.getRoleName(),roleUserForm.getUserName()),HttpStatus.CREATED);
	}
	
}
