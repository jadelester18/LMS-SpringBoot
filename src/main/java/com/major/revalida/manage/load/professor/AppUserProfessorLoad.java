package com.major.revalida.manage.load.professor;

import java.time.LocalDate;

import com.major.revalida.appuser.admin.crud.college.College;
import com.major.revalida.appuser.admin.crud.program.Program;
import com.major.revalida.appuser.admin.crud.room.Room;
import com.major.revalida.appuser.admin.crud.section.Section;
import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.faculty.AppUserProfessor;
import com.major.revalida.login.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "professor_load")
public class AppUserProfessorLoad {

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long professorLoadId;
    
	@ManyToOne
	@JoinColumn(nullable = false, name = "professor_id")
	private AppUserProfessor appUserProfessor;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "subject_id")
	private Subject subject;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "program_id")
	private Program program;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "college_id")
	private College college;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "room_id")
	private Room room;
	
	private LocalDate schoolYear;
	private Integer semester;
	private Integer yearLevel;
	private String loadUnits;
	
}
