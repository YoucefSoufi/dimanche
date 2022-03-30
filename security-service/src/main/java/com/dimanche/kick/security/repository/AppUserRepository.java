package com.dimanche.kick.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dimanche.kick.security.entites.AppUser;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUserName(String userName);
	
	@Query("select appUser from AppUser appUser left join fetch appUser.appRoles where appUser.userName=:userName")
	Optional<AppUser> findByUserNameWithRoles(String userName);

	@Query("select appUser from AppUser appUser left join fetch appUser.appRoles")
	List<AppUser> findAllWithRoles();
}
