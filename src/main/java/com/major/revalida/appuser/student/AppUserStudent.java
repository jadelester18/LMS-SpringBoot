package com.major.revalida.appuser.student;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.admin.crud.program.Program;
import com.major.revalida.appuser.admin.crud.section.Section;
import com.major.revalida.appuser.admin.crud.subject.Subject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "student_details")
public class AppUserStudent implements UserDetails {
	
		@Id
		@GeneratedValue(
				strategy = GenerationType.IDENTITY
				)
		private Long studentId;
	    private String studentNo;
	    private String password;
	    private String firstName;
	    private String middleName;
	    private String lastName;
	    private LocalDate birthdate;
		@ManyToOne
		@JoinColumn(name = "section_id")
		private Section section;
		@ManyToOne
//		@JoinColumn(nullable = false, name = "program_id")
		@JoinColumn(name = "program_id")
		private Program program;
		@ManyToOne
		@JoinColumn(name = "subject_id")
		private Subject subjects;
	    private Integer semester = 0;
	    private Integer yearLevel = 0;
	    @Column(name = "email", unique = true, nullable = false)
	    private String email;
	    @Enumerated(EnumType.STRING)
	    private AppUserRole appUserRole;
	    private String status = "PENDING";
	    private Boolean locked = false;
	    private Boolean enabled = false;
	    private String photo;
	    @Column(name = "register_at")
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date registerAt;
	    
	    
	    public AppUserStudent(String email, String password,
				String firstName, String middleName, String lastName, LocalDate birthdate, Program program, AppUserRole appUserRole) {
			super();
			this.email = email;
			this.password = password;
			this.firstName = firstName;
			this.middleName = middleName;
			this.lastName = lastName;
			this.birthdate = birthdate;
			this.program = program;
			this.appUserRole = appUserRole;
		}    
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

}
