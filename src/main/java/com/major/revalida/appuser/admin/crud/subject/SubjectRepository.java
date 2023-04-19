package com.major.revalida.appuser.admin.crud.subject;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.major.revalida.appuser.faculty.AppUserProfessor;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>  {
	Optional<Subject> findBySubjectId(Long id);

	Optional<Subject> findBySubjectCode(String prerequisiteSubjectCode);
}
