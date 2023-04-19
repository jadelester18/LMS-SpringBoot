package com.major.revalida.login.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.major.revalida.appuser.AppUserRole;


public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUserNo(String userNo);
//	Optional<User> findByAppUserRole(AppUserRole appUserRole);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndAppUserRole(String email, AppUserRole role);
	
	int countByUserNoStartingWith(String string);
}
