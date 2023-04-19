package com.major.revalida.manage.load.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.manage.load.schedules.Schedule;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
	
	boolean existsByStudentAndSchedule(AppUserStudent student, Schedule schedule);
	
	boolean existsByStudentAndSubject(AppUserStudent student, Subject subject);

	List<Enrollment> findByStudent(AppUserStudent student);
}
