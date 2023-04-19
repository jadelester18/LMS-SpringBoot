package com.major.revalida.appuser.admin.crud.degree;

import com.major.revalida.appuser.admin.crud.college.College;

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
@Table(name = "degree")
public class Degree {
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long degreeId;
    private String degreeTitle;
}
