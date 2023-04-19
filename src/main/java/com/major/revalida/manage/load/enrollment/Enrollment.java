package com.major.revalida.manage.load.enrollment;

import java.time.LocalDate;

import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.manage.load.schedules.Schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
public class Enrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private AppUserStudent student;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "subject_id")
	private Subject subject;

	private LocalDate enrollmentDate;

	public Enrollment(AppUserStudent student, Schedule schedule, LocalDate enrollmentDate, Subject subject) {
		this.student = student;
		this.schedule = schedule;
		this.enrollmentDate = enrollmentDate;
		this.subject = subject;
	}
}
