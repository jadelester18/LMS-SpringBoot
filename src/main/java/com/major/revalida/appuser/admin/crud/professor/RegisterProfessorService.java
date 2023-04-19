package com.major.revalida.appuser.admin.crud.professor;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.faculty.AppUserProfessor;
import com.major.revalida.appuser.faculty.AppUserProfessorRepository;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterProfessorService {

	private AppUserProfessorRepository appUserProfessorRepository;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;

	public AppUserProfessor createProfessor(AppUserProfessor professor) {

		boolean userEmailExist = appUserProfessorRepository.findByEmail(professor.getEmail()).isPresent();

		boolean userNameExist = appUserProfessorRepository.findByFirstNameAndMiddleNameAndLastName(professor.getFirstName(),
				professor.getMiddleName(), professor.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

		if (userNameExist) {
			throw new IllegalStateException("Your Name is Already Exist.");
		}

		String encodedPassword = passwordEncoder.encode(professor.getPassword());
		
		User user = new User(professor.getEmail(), encodedPassword,
				AppUserRole.PROFESSOR);
		user.setUserNo(professor.getProfessorNo());
		user = userRepository.save(user);
		
		professor.setPassword(encodedPassword);
		professor.setAppUserRole(AppUserRole.PROFESSOR);
		return appUserProfessorRepository.save(professor);
	}
	
	public String updateProfessorNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserProfessor professor = appUserProfessorRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Professor not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Professor not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		professor.setProfessorNo(generatedString);
		user.setUserNo(generatedString);
		appUserProfessorRepository.save(professor);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(professor.getFirstName(), generatedString));
		
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
