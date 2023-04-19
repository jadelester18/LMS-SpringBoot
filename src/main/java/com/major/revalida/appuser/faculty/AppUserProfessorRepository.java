package com.major.revalida.appuser.faculty;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserProfessorRepository extends JpaRepository<AppUserProfessor, Long>{

	Optional<AppUserProfessor> findByEmail(String email);
	Optional<AppUserProfessor> findByProfessorNo(String professorNo);
	
	
	Optional<AppUserProfessor> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);


}
