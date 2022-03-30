package com.dimanche.kick.security.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dimanche.kick.security.entites.AppRole;

@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	
	Optional<AppRole> findByRoleName(String roleName);

}
