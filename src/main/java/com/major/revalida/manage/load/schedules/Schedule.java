package com.major.revalida.manage.load.schedules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.major.revalida.appuser.admin.crud.college.College;
import com.major.revalida.appuser.admin.crud.curriculum.Curriculum;
import com.major.revalida.appuser.admin.crud.program.Program;
import com.major.revalida.appuser.admin.crud.room.Room;
import com.major.revalida.appuser.admin.crud.section.Section;
import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.faculty.AppUserProfessor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "curriculum_id")
	private Curriculum curriculum;
	
	@Column(nullable = false)
	private LocalDate schoolYear;
	
	@Column(nullable = false)
	private Integer yearLevel;
	
	@Column(nullable = false)
	private Integer semester;

	@Column(nullable = false)
	private DayOfWeek day;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "college_id")
	private College college;   
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "program_id")
	private Program program;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "subject_id")
	private Subject subject;
	
	@ManyToOne(optional = true)
	@JoinColumn(nullable = true, name = "room_id")
	private Room room;
	
	@ManyToOne
	@JoinColumn(nullable = true, name = "professor_id")
	private AppUserProfessor professor;
	
}
