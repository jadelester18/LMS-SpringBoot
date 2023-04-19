package com.major.revalida.login.user;

import com.major.revalida.appuser.AppUserRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	private String token;
    private String firstname;
    private String middlename;
    private String lastname;
	private String usernumber;
	private Boolean enabled;
	@Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
}
