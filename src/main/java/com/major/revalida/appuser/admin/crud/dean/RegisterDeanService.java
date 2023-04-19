package com.major.revalida.appuser.admin.crud.dean;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.dean.AppUserDean;
import com.major.revalida.appuser.dean.AppUserDeanRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterDeanService {

	private AppUserDeanRepository appUserDeanRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserDean createDean(AppUserDean dean) {

		boolean userEmailExist = appUserDeanRepository.findByEmail(dean.getEmail()).isPresent();

		boolean userNameExist = appUserDeanRepository.findByFirstNameAndMiddleNameAndLastName(dean.getFirstName(),
				dean.getMiddleName(), dean.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(dean.getPassword());
		
		User user = new User(dean.getEmail(), encodedPassword,
				AppUserRole.DEAN);
		user.setUserNo(dean.getDeanNo());
		user = userRepository.save(user);
		
		dean.setPassword(encodedPassword);
		dean.setAppUserRole(AppUserRole.DEAN);
		return appUserDeanRepository.save(dean);
	}
	
	public String updateDeanNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserDean dean = appUserDeanRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Dean not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Dean not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		dean.setDeanNo(generatedString);
		user.setUserNo(generatedString);
		appUserDeanRepository.save(dean);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(dean.getFirstName(), generatedString));
		
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
