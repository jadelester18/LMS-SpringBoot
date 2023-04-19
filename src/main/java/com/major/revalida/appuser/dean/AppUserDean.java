package com.major.revalida.appuser.dean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.admin.crud.college.College;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "dean_details")
public class AppUserDean implements UserDetails{
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
			)
	private Long deanId;
    private String deanNo;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthdate;
//    Added new columns
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    @ManyToOne
    @JoinColumn(nullable = false, name = "college_id")
    private College college;
    private Boolean locked = false;
    private Boolean enabled = false;
    private String photo;
	
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
