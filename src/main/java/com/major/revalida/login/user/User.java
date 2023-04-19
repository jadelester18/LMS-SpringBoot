package com.major.revalida.login.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.login.user.token.Token;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
	
	 	@Id
	 	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	 	
	 	
	 	@Column(name = "user_no", unique = true)
	    private String userNo;
	 	@Column(name = "email", unique = true, nullable = false)
	    private String email;
	    private String password;
	    @Enumerated(EnumType.STRING)
	    private AppUserRole appUserRole; 
	    @OneToMany(mappedBy = "user")
	    private List<Token> tokens;
	    
	    
	    
	public User(String email, String password, AppUserRole appUserRole) 	{
		super();
		this.email = email;
		this.password = password;
		this.appUserRole = appUserRole;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(appUserRole.name()));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userNo;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public AppUserRole getAppUserRole() {
		return appUserRole;
	}

}

