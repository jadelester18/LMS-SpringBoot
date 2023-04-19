package com.major.revalida.appuser.dean;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.admission.AppUserAdmission;
import com.major.revalida.appuser.subadmin.AppUserSubAdmin;

@Repository
@Transactional(readOnly = true)
public interface AppUserDeanRepository extends JpaRepository<AppUserDean, Long>{

	Optional<AppUserDean> findByEmail(String email);
	Optional<AppUserDean> findByDeanNo(String deanNo);
	
	
	Optional<AppUserDean> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
	
}
