package com.major.revalida.registration;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.major.revalida.appuser.student.AppUserStudent;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api/v1/auth")
@AllArgsConstructor
public class RegistrationController {
	
	private final RegistrationService registrationService;
	
	@PostMapping("/registration")
	public String register(@RequestBody RegistrationRequest request) {
		return registrationService.register(request);
	}
	
	@PostMapping(path = "/resend-otp")
	public String resendotp(@RequestBody RegistrationRequest request) {
	    return registrationService.resendotptoken(request.getEmail());
	}
	
	@GetMapping(path = "/confirm")
	public String confirm(@RequestParam("token") String token) {
	    return registrationService.confirmToken(token);
	}


}
