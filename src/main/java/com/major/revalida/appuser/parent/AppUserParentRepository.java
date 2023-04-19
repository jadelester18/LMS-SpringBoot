package com.major.revalida.appuser.parent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.faculty.AppUserProfessor;
import com.major.revalida.appuser.student.AppUserStudent;

@Repository
@Transactional(readOnly = true)
public interface AppUserParentRepository extends JpaRepository<AppUserParent, Long>{

	Optional<AppUserParent> findByEmail(String email);
	Optional<AppUserParent> findByParentNo(String professorNo);
	
	
	Optional<AppUserParent> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
	
}
