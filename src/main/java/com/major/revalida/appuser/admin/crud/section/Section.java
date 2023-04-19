package com.major.revalida.appuser.admin.crud.section;

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
@Table(name = "section")
public class Section {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
    private Long sectionId;
    private String sectionName;
    
}
