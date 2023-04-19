package com.major.revalida.appuser.admin.crud.curriculum;

import java.time.LocalDate;

import com.major.revalida.appuser.admin.crud.college.College;

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
@Table(name = "curriculum")
public class Curriculum {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long curriculumId;
	
	@Column(nullable = false)
	private String curriculumName;

	@Column(nullable = false)
	private LocalDate curriculumCreatedYear;

	@Column(nullable = true)
	private String curriculumDescription;
	
	
	
}
