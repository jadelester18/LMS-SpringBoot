package com.major.revalida.appuser.subadmin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.admission.AppUserAdmission;
import com.major.revalida.appuser.parent.AppUserParent;

@Repository
@Transactional(readOnly = true)
public interface AppUserSubAdminRepository extends JpaRepository<AppUserSubAdmin, Long>{

	Optional<AppUserSubAdmin> findByEmail(String email);
	Optional<AppUserSubAdmin> findBySubAdminNo(String subAdminNo);
	
	
	Optional<AppUserSubAdmin> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
	
}
