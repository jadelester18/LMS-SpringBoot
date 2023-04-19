package com.major.revalida.appuser.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface AppUserStudentRepository extends JpaRepository<AppUserStudent, Long> {
	
	Optional<AppUserStudent> findByEmail(String email);
	
	Optional<AppUserStudent> findByStudentNo(String studentNo);
	
	Optional<AppUserStudent> findByFirstName(String firstname);
	
	Optional<AppUserStudent> findByEnabled(Boolean enabled);
	
	Optional<AppUserStudent> findByFirstNameAndMiddleNameAndLastName(String firstname, String middlename, String lastname);

	
	//If the user verified their account update enabled column to true
	 @Transactional
	    @Modifying
	    @Query("UPDATE AppUserStudent a " +
	            "SET a.enabled = TRUE WHERE a.email = ?1")
	    int enableAppUser(String email);

	

	 

	
}
