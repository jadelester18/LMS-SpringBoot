package com.major.revalida.login.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {
	
	private final AuthenticateService service;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request)); 
	}
	
	@PostMapping("/authenticate-admin")
	public ResponseEntity<AuthenticationResponse> loginAdmin(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticateAdmin(request)); 
	}
	
	@PostMapping("/authenticate-email")
	public ResponseEntity<AuthenticationResponse> emailLogin(@RequestBody EmailAuthenticationRequest request) {
	    return ResponseEntity.ok(service.authenticateEmail(request));
	}
	
}
