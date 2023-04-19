package com.major.revalida.appuser.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.subadmin.AppUserSubAdmin;


@Repository
@Transactional(readOnly = true)
public interface AppUserAdminRepository extends JpaRepository<AppUserAdmin, Long> {
	
	Optional<AppUserAdmin> findByEmail(String email);
	Optional<AppUserAdmin> findByAdminNo(String adminNo);
	
	
	Optional<AppUserAdmin> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
	
}
