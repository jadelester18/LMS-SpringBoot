package com.major.revalida.appuser.faculty.load.history;

import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.faculty.AppUserProfessor;
import com.major.revalida.manage.load.professor.AppUserProfessorLoad;

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
@Table(name = "t_subject_detail_history")
public class SubjectDetailsHistory {

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
//    private Long professorId;
//    private Long subjectId;
    private String academicYear;
    private Integer sem;
    private String schedule;
    private String section;
    private Integer yearlevel;
    private String status;
    private String activeDeactive;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "professor_id")
	private AppUserProfessor appUserProfessor;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "subject_id")
	private Subject subject;
    
}
