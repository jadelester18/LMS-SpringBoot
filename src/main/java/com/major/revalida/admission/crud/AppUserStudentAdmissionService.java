package com.major.revalida.admission.crud;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.admin.crud.section.SectionRepository;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserStudentAdmissionService implements UserDetailsService {
	
	private final static String USER_NOT_FOUND_MSG = "User email %s not found.";
	private final AppUserStudentRepository appUserStudentRepository;
	private final UserRepository userRepository;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	private final SectionRepository sectionRepository;
	
	private final AppUserNumberGenerator appUserNumberGenerator;
	
		@Override
		public AppUserStudent loadUserByUsername(String email) throws UsernameNotFoundException {
			return appUserStudentRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
		}

	public List<AppUserStudent> getAllStudents() {
		return appUserStudentRepository.findAll();
	}

	public AppUserStudent getStudentById(Long studentId) {
		return appUserStudentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + studentId));
	}

	public void updateStudent(Long studentId, AppUserStudent updatedStudent) {
		AppUserStudent student = appUserStudentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalStateException("Student not found"));
		student.setFirstName(updatedStudent.getFirstName());
		student.setLastName(updatedStudent.getLastName());
		student.setFirstName(updatedStudent.getFirstName());
		appUserStudentRepository.save(student);
	}
	
	public String updateStudentNumber(String email) {
		boolean isValidEmail = emailValidator.test(email);
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		
		AppUserStudent student = appUserStudentRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Student not found"));
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Student not found"));
		
		String generatedString = appUserNumberGenerator.generateUserNumber();
		
		student.setStudentNo(generatedString);
		
		
		user.setUserNo(generatedString);
		
		appUserStudentRepository.save(student);
		userRepository.save(user);
	
		emailSender.send(
				email,
				buildEmailUserNo(student.getFirstName(), generatedString));
		
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
