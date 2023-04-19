package com.major.revalida.appuser.admin.crud.program;

import com.major.revalida.appuser.admin.crud.college.College;
import com.major.revalida.appuser.admin.crud.degree.Degree;

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
@Table(name = "program")
public class Program {
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long programId;
    private String programCode;
    private String programTitle;
    
    @ManyToOne
    @JoinColumn(nullable = false, name = "collegeId")
    private College college;
    @ManyToOne
    @JoinColumn(nullable = false, name = "degreeId")
    private Degree degree;
    
    
}
