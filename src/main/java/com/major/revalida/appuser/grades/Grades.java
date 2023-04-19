package com.major.revalida.appuser.grades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.major.revalida.appuser.faculty.load.history.SubjectDetailsHistory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "grades")
public class Grades {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long gradeId;
    private Integer sessionId;
    private BigDecimal grade;
    private String comment;
    private LocalDateTime dateModified;
    private String remarks;
    private String status;
    private Integer professorId;
	
}
