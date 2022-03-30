package com.dimanche.kick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.dimanche.kick.security.entites.AppRole;
import com.dimanche.kick.security.entites.AppUser;
import com.dimanche.kick.security.repository.AppRoleRepository;
import com.dimanche.kick.security.repository.AppUserRepository;
import com.dimanche.kick.security.service.AccountService;
import com.dimanche.kick.security.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@Slf4j
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

	
	@Bean
	CommandLineRunner start (AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, AccountService accountService, SecurityUtil securityUtil) {
	
		List<AppRole> appRoles = gerenrateurListRoles(appRoleRepository);
		
		List<AppUser> appUsers =  generationListUsers(appRoles);
		
		appRoleRepository.findAll().forEach(System.out::println);
		log.debug("***********Roles**********\n");

		log.debug("***********Users**********Before\n");
		appUsers.forEach(System.out::println);
		accountService.addNewUsers(appUsers);
		log.debug("***********Users**********After\n");
		accountService.usersWithRoles().forEach(System.out::println);
		return null;
	}

	private List<AppRole> gerenrateurListRoles(AppRoleRepository appRoleRepository) {

		AppRole appRoleAdmin = new AppRole(null,"Admin");
		AppRole appRoleSuperAdmin = new AppRole(null,"SuperAdmin");
		AppRole appRoleNoob = new AppRole(null,"Noob");
		List<AppRole> appRoles = Arrays.asList(appRoleAdmin,appRoleSuperAdmin,appRoleNoob);
		return appRoleRepository.saveAll(appRoles);
	}


	private List<AppUser> generationListUsers(List<AppRole> appRoles) {
		List<AppUser> appUsers = new ArrayList<>();
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
		for (int x = 0; x < 10; x++) {
		    Random rand = new Random();
		    AppRole randomRole = appRoles.get(rand.nextInt(appRoles.size()));
		    
		    String generatedString = random.ints(leftLimit, rightLimit + 1)
		  	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  	      .limit(targetStringLength)
		  	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  	      .toString();
		    appUsers.add(new AppUser(null,generatedString,generatedString,Collections.singleton(randomRole)));
		}
		return appUsers;
	}
}
