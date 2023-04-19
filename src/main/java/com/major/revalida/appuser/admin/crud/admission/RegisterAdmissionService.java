package com.major.revalida.appuser.admin.crud.admission;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.admission.AppUserAdmission;
import com.major.revalida.appuser.admission.AppUserAdmissionRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterAdmissionService {

	private AppUserAdmissionRepository appUserAdmissionRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserAdmission createAdmission(AppUserAdmission admission) {

		boolean userEmailExist = appUserAdmissionRepository.findByEmail(admission.getEmail()).isPresent();

		boolean userNameExist = appUserAdmissionRepository.findByFirstNameAndMiddleNameAndLastName(admission.getFirstName(),
				admission.getMiddleName(), admission.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(admission.getPassword());
		
		User user = new User(admission.getEmail(), encodedPassword,
				AppUserRole.ADMISSION);
		user.setUserNo(admission.getAdmissionNo());
		user = userRepository.save(user);
		
		admission.setPassword(encodedPassword);
		admission.setAppUserRole(AppUserRole.ADMISSION);
		return appUserAdmissionRepository.save(admission);
	}
	
	public String updateAdmissionNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserAdmission admission = appUserAdmissionRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Admission not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Admission not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		admission.setAdmissionNo(generatedString);
		user.setUserNo(generatedString);
		appUserAdmissionRepository.save(admission);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(admission.getFirstName(), generatedString));
		
		return generatedString;
	}
	
	@Autowired
	private ResourceLoader resourceLoader;

	private String loadEmailTemplate() {
	    try {
	        Resource resource = resourceLoader.getResource("classpath:templates/user-no-email.html");
	        return new String(Files.readAllBytes(resource.getFile().toPath()));
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to load email template", e);
	    }
	}
	
	private String buildEmailUserNo(String name, String code) {
	    String emailTemplate = loadEmailTemplate();
	    return emailTemplate
	        .replace("${name}", name)
	        .replace("${code}", code);
	}
	
}
