package com.major.revalida.login.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/open")
@RequiredArgsConstructor
public class DashboardController {

	@GetMapping("/demo-controller")
	public ResponseEntity<String> sayHello() {
		return ResponseEntity.ok("Hi Welcome to our website");
	}
	
}
