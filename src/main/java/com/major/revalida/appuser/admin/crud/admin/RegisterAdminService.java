package com.major.revalida.appuser.admin.crud.admin;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.admin.AppUserAdmin;
import com.major.revalida.appuser.admin.AppUserAdminRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterAdminService {

	private AppUserAdminRepository appUserAdminRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserAdmin createAdmin(AppUserAdmin admin) {

		boolean userEmailExist = appUserAdminRepository.findByEmail(admin.getEmail()).isPresent();

		boolean userNameExist = appUserAdminRepository.findByFirstNameAndMiddleNameAndLastName(admin.getFirstName(),
				admin.getMiddleName(), admin.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(admin.getPassword());
		
		User user = new User(admin.getEmail(), encodedPassword,
				AppUserRole.ADMIN);
		user.setUserNo(admin.getAdminNo());
		user = userRepository.save(user);
		
		admin.setPassword(encodedPassword);
		admin.setAppUserRole(AppUserRole.ADMIN);
		return appUserAdminRepository.save(admin);
	}
	
	public String updateAdminNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserAdmin admin = appUserAdminRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Admin not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Admin not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		admin.setAdminNo(generatedString);
		user.setUserNo(generatedString);
		appUserAdminRepository.save(admin);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(admin.getFirstName(), generatedString));
		
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
