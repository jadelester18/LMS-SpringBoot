package com.major.revalida.manage.load.enrollment;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.major.revalida.appuser.admin.crud.subject.Subject;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectDto {
	private Long scheduleId;
	private String curriculumName;
	private String collegeName;
	private String programTitle;
	private String sectionName;
	private Integer yearLevel;
	private Integer semester;
	private DayOfWeek day;
	private LocalTime startTime;
	private LocalTime endTime;
	private String roomName;
	private String subjectCode;
	private String subjectTitle;
	private Integer units;
	private Subject prerequisiteSubject;
	private String firstName;
	private String lastName;
	private boolean selected;

	public SubjectDto(Long scheduleId, String curriculumName, String collegeName, String programTitle,
			String sectionName, Integer yearLevel, Integer semester, DayOfWeek day, LocalTime startTime,
			LocalTime endTime, String roomName, String subjectCode, String subjectTitle, Integer units,
			Subject prerequisiteSubject, String firstName, String lastName, boolean selected) {
		this.scheduleId = scheduleId;
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
		this.subjectCode = subjectCode;
		this.subjectTitle = subjectTitle;
		this.units = units;
		this.prerequisiteSubject = prerequisiteSubject;
		this.firstName = firstName;
		this.lastName = lastName;
		this.selected = selected;
	}
}