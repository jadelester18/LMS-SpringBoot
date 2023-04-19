package com.major.revalida.appuser.admin.crud.college;

import com.major.revalida.appuser.admin.crud.program.Program;

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
@Table(name = "college")
public class College {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long collegeId;
    private String collegeName;
	
}
