package com.major.revalida.manage.load.enrollment;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnrolledSubjectDto {
	private Long id;
	private String subjectCode;
	private String subjectTitle;
	private int units;
	private String curriculumName;
	private String collegeName;
	private String programTitle;
	private String sectionName;
	private int yearLevel;
	private int semester;
	private DayOfWeek day;
	private LocalTime startTime;
	private LocalTime endTime;
	private String roomName;
	private String professorFirstName;
	private String professorLastName;
	
	public EnrolledSubjectDto(Long id, String subjectCode, String subjectTitle, int units, String curriculumName,
			String collegeName, String programTitle, String sectionName, int yearLevel, int semester, DayOfWeek day,
			LocalTime startTime, LocalTime endTime, String roomName, String professorFirstName,
			String professorLastName) {
		super();
		this.id = id;
		this.subjectCode = subjectCode;
		this.subjectTitle = subjectTitle;
		this.units = units;
		this.curriculumName = curriculumName;
		this.collegeName = collegeName;
		this.programTitle = programTitle;
		this.sectionName = sectionName;
		this.yearLevel = yearLevel;
		this.semester = semester;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomName = roomName;
		this.professorFirstName = professorFirstName;
		this.professorLastName = professorLastName;
	}
	
	
	
}
