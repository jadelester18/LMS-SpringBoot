package com.major.revalida.forgetpassword;

import com.major.revalida.login.user.EmailAuthenticationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
	private String email;
}
