package com.major.revalida.login.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.admin.AppUserAdminRepository;
import com.major.revalida.appuser.admission.AppUserAdmissionRepository;
import com.major.revalida.appuser.dean.AppUserDeanRepository;
import com.major.revalida.appuser.faculty.AppUserProfessorRepository;
import com.major.revalida.appuser.parent.AppUserParentRepository;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.appuser.subadmin.AppUserSubAdminRepository;
import com.major.revalida.login.user.token.Token;
import com.major.revalida.login.user.token.TokenRepository;
import com.major.revalida.login.user.token.TokenType;
import com.major.revalida.security.provider.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final TokenRepository tokenRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository repository;
	private final AppUserStudentRepository appUserStudentRepository;
	private final AppUserParentRepository appUserParentRepository;
	private final AppUserAdmissionRepository appUserAdmissionRepository;
	private final AppUserProfessorRepository appUserProfessorRepository;
	private final AppUserDeanRepository appUserDeanRepository;
	private final AppUserSubAdminRepository appUserSubAdminRepository;
	private final AppUserAdminRepository appUserAdminRepository;

	public AuthenticationResponse authenticateEmail(EmailAuthenticationRequest request) {
		// Finding the email in the user table
		var user = repository.findByEmail(request.getEmail().toLowerCase())
				.orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		// Finding the email in the student table
		AppUserStudent student = appUserStudentRepository.findByEmail(request.getEmail().toLowerCase())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (!student.isEnabled()) {
			throw new DisabledException("User not verified");
		}

		if (!user.getAppUserRole().equals(AppUserRole.STUDENT)) {
			throw new BadCredentialsException("Invalid user role");
		}
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid email or password");
		}
		
		return AuthenticationResponse.builder().token(jwtToken).firstname(student.getFirstName())
				.middlename(student.getMiddleName()).lastname(student.getLastName())
				.usernumber(student.getStudentNo()).enabled(student.isEnabled()).appUserRole(AppUserRole.STUDENT)
				.build();
		
//		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserNo(), request.getPassword()));

		var user = repository.findByUserNo(request.getUserNo())
				.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}

		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		switch (user.getAppUserRole()) {
		case STUDENT:
			var student = appUserStudentRepository.findByStudentNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!student.isEnabled()) {
				throw new DisabledException("User is disabled");
			}
			return AuthenticationResponse.builder().token(jwtToken).firstname(student.getFirstName())
					.middlename(student.getMiddleName()).lastname(student.getLastName())
					.usernumber(student.getStudentNo()).enabled(student.isEnabled()).appUserRole(AppUserRole.STUDENT)
					.build();

		case PARENT:
			var parent = appUserParentRepository.findByParentNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!parent.isEnabled()) {
				throw new DisabledException("User is disabled");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(parent.getFirstName())
					.middlename(parent.getMiddleName()).lastname(parent.getLastName()).usernumber(parent.getParentNo())
					.enabled(parent.isEnabled()).appUserRole(AppUserRole.STUDENT).build();

		case ADMISSION:
			var admission = appUserAdmissionRepository.findByAdmissionNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!admission.isEnabled()) {
				throw new DisabledException("User is disabled");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(admission.getFirstName())
					.middlename(admission.getMiddleName()).lastname(admission.getLastName())
					.usernumber(admission.getAdmissionNo()).enabled(admission.isEnabled())
					.appUserRole(AppUserRole.STUDENT).build();

		case PROFESSOR:
			var professor = appUserProfessorRepository.findByProfessorNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!professor.isEnabled()) {
				throw new DisabledException("User is disabled");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(professor.getFirstName())
					.middlename(professor.getMiddleName()).lastname(professor.getLastName())
					.usernumber(professor.getProfessorNo()).enabled(professor.isEnabled())
					.appUserRole(AppUserRole.STUDENT).build();

		case DEAN:
			var dean = appUserDeanRepository.findByDeanNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!dean.isEnabled()) {
				throw new DisabledException("User not verified");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(dean.getFirstName())
					.middlename(dean.getMiddleName()).lastname(dean.getLastName()).usernumber(dean.getDeanNo())
					.enabled(dean.isEnabled()).appUserRole(AppUserRole.STUDENT).build();

		default:
			throw new BadCredentialsException("Invalid user role");
		}

	}

	public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserNo(), request.getPassword()));

		var user = repository.findByUserNo(request.getUserNo())
				.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}

		switch (user.getAppUserRole()) {
		case SUBADMIN:
			var subadmin = appUserSubAdminRepository.findBySubAdminNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!subadmin.isEnabled()) {
				throw new DisabledException("User is disabled");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(subadmin.getFirstName())
					.middlename(subadmin.getMiddleName()).lastname(subadmin.getLastName())
					.usernumber(subadmin.getSubAdminNo()).enabled(subadmin.isEnabled()).appUserRole(AppUserRole.STUDENT)
					.build();

		case ADMIN:
			var admin = appUserAdminRepository.findByAdminNo(user.getUserNo())
					.orElseThrow(() -> new BadCredentialsException("Invalid User number"));

			if (!admin.isEnabled()) {
				throw new DisabledException("User is disabled");
			}

			return AuthenticationResponse.builder().token(jwtToken).firstname(admin.getFirstName())
					.middlename(admin.getMiddleName()).lastname(admin.getLastName()).usernumber(admin.getAdminNo())
					.enabled(admin.isEnabled()).appUserRole(AppUserRole.STUDENT).build();
		default:
			throw new BadCredentialsException("Invalid user role");
		}
//        return AuthenticationResponse.builder().token(jwtToken).build();
	}

	private void saveUserToken(User user, String jwtToken) {
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

}
