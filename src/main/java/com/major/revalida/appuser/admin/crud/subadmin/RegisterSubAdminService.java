package com.major.revalida.appuser.admin.crud.subadmin;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.subadmin.AppUserSubAdmin;
import com.major.revalida.appuser.subadmin.AppUserSubAdminRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterSubAdminService {

	private AppUserSubAdminRepository appUserSubAdminRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserSubAdmin createSubAdmin(AppUserSubAdmin subAdmin) {

		boolean userEmailExist = appUserSubAdminRepository.findByEmail(subAdmin.getEmail()).isPresent();

		boolean userNameExist = appUserSubAdminRepository.findByFirstNameAndMiddleNameAndLastName(subAdmin.getFirstName(),
				subAdmin.getMiddleName(), subAdmin.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(subAdmin.getPassword());
		
		User user = new User(subAdmin.getEmail(), encodedPassword,
				AppUserRole.SUBADMIN);
		user.setUserNo(subAdmin.getSubAdminNo());
		user = userRepository.save(user);
		
		subAdmin.setPassword(encodedPassword);
		subAdmin.setAppUserRole(AppUserRole.SUBADMIN);
		return appUserSubAdminRepository.save(subAdmin);
	}
	
	public String updateSubAdminNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserSubAdmin subAdmin = appUserSubAdminRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("SubAdmin not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("SubAdmin not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		subAdmin.setSubAdminNo(generatedString);
		user.setUserNo(generatedString);
		appUserSubAdminRepository.save(subAdmin);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(subAdmin.getFirstName(), generatedString));
		
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
