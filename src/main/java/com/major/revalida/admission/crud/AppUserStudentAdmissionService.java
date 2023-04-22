package com.major.revalida.admission.crud;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.appuser.admin.crud.section.Section;
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

	public static final Integer MAX_STUDENTS_PER_SECTION = 2;

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
		if (!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}

		AppUserStudent student = appUserStudentRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Student not found"));

		User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("Student not found"));

		if (student != null && student.getStudentNo() != null) {
			throw new IllegalStateException("Student already have student number.");
		}
		
		if (student != null && student.getSection() != null) {
			throw new IllegalStateException("Student already have section.");
		}

	    // Check if the student's yearLevel is 0 to determine that the student is a new user.
	    boolean isNewUser = student.getYearLevel() == 0;

	    // Generate a new section (A, B, C, ..) if there is no available section found in section table.
	    Section availableSection = sectionRepository.findFirstByOrderBySectionName();
	    if (availableSection == null) {
	        availableSection = new Section();
	        availableSection.setSectionName("A");
	        sectionRepository.save(availableSection);
	    }

	    // If there is an available section, find all students having also this section.
	    String sectionName = availableSection.getSectionName();
	    List<AppUserStudent> studentsInSection = appUserStudentRepository.findByProgramAndYearLevelAndSection_SectionName(
	    		student.getProgram(),
	    		student.getYearLevel(),
	            sectionName
	    );

	    // Check if the total of students with the same program, year level, and section exceeds the maximum student per section.
	    boolean sectionFull = studentsInSection.size() >= MAX_STUDENTS_PER_SECTION;

	    // If the section is full, create a new section and assign it to the student.
	    if (sectionFull) {
	        char newSectionName = (char) (sectionName.charAt(0) + 1);
	        Section newSection = new Section();
	        newSection.setSectionName(Character.toString(newSectionName));
	        sectionRepository.save(newSection);
	        student.setSection(newSection);
	    } else {
	    	student.setSection(availableSection);
	    }
		
	    // Assign a student number to the student.
	    String generatedString = appUserNumberGenerator.generateUserNumber();
	    student.setStudentNo(generatedString);
	    student.setRegisterAt(new Date());
	    
	    user.setUserNo(generatedString);

	    // Save the updated student record to the database.
	    userRepository.save(user);
	    appUserStudentRepository.save(student);
	    return "Assigning Section and Student Number Successful.";
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
		return emailTemplate.replace("${name}", name).replace("${code}", code);
	}

}
