package com.major.revalida.appuser.admission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.parent.AppUserParent;

@Repository
@Transactional(readOnly = true)
public interface AppUserAdmissionRepository extends JpaRepository<AppUserAdmission, Long>{

	Optional<AppUserAdmission> findByEmail(String email);
	Optional<AppUserAdmission> findByAdmissionNo(String admissionNo);
	
	
	Optional<AppUserAdmission> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
	
}
