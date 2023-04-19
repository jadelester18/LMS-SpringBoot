package com.major.revalida.appuser.admin.crud.subject;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "subject")
public class Subject {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long subjectId;
    private String subjectCode;
    private String subjectTitle;
    private Integer units;
    private String activeDeactive;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime lengthOfDiscussions;
    private String lengthOfDiscussionsHour;
    private String lengthOfDiscussionsMinute;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "prerequisite_subject_id")
    private Subject prerequisiteSubject;

	
    
    
}
