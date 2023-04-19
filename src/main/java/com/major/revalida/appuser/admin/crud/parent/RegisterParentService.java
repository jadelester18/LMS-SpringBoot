package com.major.revalida.appuser.admin.crud.parent;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.parent.AppUserParent;
import com.major.revalida.appuser.parent.AppUserParentRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterParentService {

	private AppUserParentRepository appUserParentRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserParent createParent(AppUserParent parent) {

		boolean userEmailExist = appUserParentRepository.findByEmail(parent.getEmail()).isPresent();

		boolean userNameExist = appUserParentRepository.findByFirstNameAndMiddleNameAndLastName(parent.getFirstName(),
				parent.getMiddleName(), parent.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(parent.getPassword());
		
		User user = new User(parent.getEmail(), encodedPassword,
				AppUserRole.PARENT);
		user.setUserNo(parent.getParentNo());
		user = userRepository.save(user);
		
		parent.setPassword(encodedPassword);
		parent.setAppUserRole(AppUserRole.PARENT);
		return appUserParentRepository.save(parent);
	}
	
	public String updateParentNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserParent parent = appUserParentRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Parent not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Parent not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		parent.setParentNo(generatedString);
		user.setUserNo(generatedString);
		appUserParentRepository.save(parent);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(parent.getFirstName(), generatedString));
		
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
