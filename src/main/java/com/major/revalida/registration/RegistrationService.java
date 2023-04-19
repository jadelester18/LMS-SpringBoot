package com.major.revalida.registration;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.appuser.student.AppUserStudentService;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.token.ConfirmationToken;
import com.major.revalida.registration.token.ConfirmationTokenService;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {
	
	private final AppUserStudentService appUserService;
	private final EmailValidator emailValidator;
	private final ConfirmationTokenService confirmationTokenService;
	private final EmailSender emailSender;
	private final AppUserStudentRepository appUserRepository;
	private final UserRepository userRepository;

	public String register(RegistrationRequest request) {
		boolean isValidEmail = emailValidator.test(request.getEmail());
		
		if(!isValidEmail) {
			throw new IllegalStateException("Email is not valid.");
		}
		 String token = appUserService.signUpUser(new AppUserStudent(
				request.getEmail().toLowerCase(),
				request.getPassword(),
				request.getFirstName().toLowerCase(), 
				request.getMiddleName().toLowerCase(),
				request.getLastName().toLowerCase(),
				request.getBirthdate(),
				request.getProgram(),
				AppUserRole.STUDENT
				),
				new User(
						request.getEmail().toLowerCase(), 
						request.getPassword(), 
						AppUserRole.STUDENT
						 )
				 );
		
		  String link = "http://localhost:2023/api/v1/auth/confirm?token=" + token;
	        emailSender.send(
	                request.getEmail(),
	                buildEmail(request.getFirstName(), link));

	        return token;
	}
	
	
	public String resendotptoken(String email) {
	    boolean isValidEmail = emailValidator.test(email);
	    if (!isValidEmail) {
	        throw new IllegalStateException("Email is not valid.");
	    }

	    Optional<AppUserStudent> optionalAppUser = appUserRepository.findByEmail(email);
	    
	    if (!optionalAppUser.isPresent()) {
	        throw new IllegalStateException("User with email " + email + " not found.");
	    }
	    
	    Optional<User> userOptional = userRepository.findByEmail(email);
	    
	    AppUserStudent appUser = optionalAppUser.get();
	    User user = userOptional.get();

	    // Saving new token after resending OTP
	    String token = UUID.randomUUID().toString();
	    ConfirmationToken confirmationToken = new ConfirmationToken(
	        token,
	        LocalDateTime.now(),
	        LocalDateTime.now().plusMinutes(15),
	        user
	    );
	    confirmationTokenService.saveConfirmationToken(confirmationToken);

	    String link = "http://localhost:2023/api/v1/auth/confirm?token=" + token;
	    emailSender.send(
	        email,
	        buildEmail(appUser.getFirstName(), link)
	    );

	    return token;
	}

	
	
	@Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

	@Autowired
	private ResourceLoader resourceLoader;

	private String loadEmailTemplate() {
	    try {
	        Resource resource = resourceLoader.getResource("classpath:templates/registration-email.html");
	        return new String(Files.readAllBytes(resource.getFile().toPath()));
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to load email template", e);
	    }
	}
	
	private String buildEmail(String name, String link) {
	    String emailTemplate = loadEmailTemplate();
	    return emailTemplate
	        .replace("${name}", name)
	        .replace("${link}", link);
	}


    
	
}
