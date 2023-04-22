package com.major.revalida.appuser.student;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserNumberGenerator;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.EmailValidator;
import com.major.revalida.registration.token.ConfirmationToken;
import com.major.revalida.registration.token.ConfirmationTokenRepository;
import com.major.revalida.registration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserStudentService implements UserDetailsService {

	private final static String USER_NOT_FOUND_MSG = "User email %s not found.";
	private final AppUserStudentRepository appUserStudentRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final UserRepository userRepository;
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final EmailSender emailSender;
	private final EmailValidator emailValidator;
	
	private final AppUserNumberGenerator appUserNumberGenerator;
	
	 private AtomicInteger sequence;

	@Override
	public AppUserStudent loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserStudentRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
	}

	public String signUpUser(AppUserStudent appUser, User user) {
		boolean userEmailExist = appUserStudentRepository.findByEmail(appUser.getEmail()).isPresent();

//		boolean userNameExist = appUserStudentRepository.findByFirstNameAndMiddleNameAndLastName(appUser.getFirstName(),
//				appUser.getMiddleName(), appUser.getLastName()).isPresent();

		if (userEmailExist) {
			throw new IllegalStateException("Email Already Taken.");
		}

//		if (userNameExist) {
//			throw new IllegalStateException("Your Name is Already Exist.");
//		}

		String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

		appUser.setPassword(encodedPassword);

		appUserStudentRepository.save(appUser);

		// Getting the id of student registered
		Long studentRegistedId = appUser.getStudentId();

		user.setPassword(encodedPassword);
//		user.setStudentId(studentRegistedId);

		userRepository.save(user);

		// Saving token after reigster
		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15), user);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		return token;
	}

	public int enableAppUser(String email) {
		return appUserStudentRepository.enableAppUser(email);
	}

}
