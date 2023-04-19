package com.major.revalida.forgetpassword;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.appuser.student.AppUserStudentService;
import com.major.revalida.email.provider.EmailSender;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;
import com.major.revalida.registration.token.ConfirmationToken;
import com.major.revalida.registration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/auth")
@AllArgsConstructor
public class ForgotPasswordController {

    private final AppUserStudentService appUserStudentService;
    private final UserRepository userRepository;
    private final AppUserStudentRepository appUserStudentRepository;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<AppUserStudent> appUserStudentOptional = appUserStudentRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email " + email + " does not exist");
        }

        User user = userOptional.get();
        AppUserStudent appUserStudent = appUserStudentOptional.get();
        String token = UUID.randomUUID().toString();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(15);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                now,
                expiresAt,
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:2023/api/v1/auth/forgot-password/reset-password?token=" + token;

        emailSender.send(
                email,
                buildEmail(appUserStudent.getFirstName(), link));

//        return ResponseEntity.ok().build();
        return link;
    }

    @PutMapping(path = "/forgot-password/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenService.getToken(token);

        if (confirmationTokenOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
        }

        User user = confirmationToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
        
     // Update password for corresponding AppUserStudent entity
        Optional<AppUserStudent> appUserStudentOptional = appUserStudentRepository.findByEmail(user.getEmail());
        if (appUserStudentOptional.isPresent()) {
            AppUserStudent appUserStudent = appUserStudentOptional.get();
            appUserStudent.setPassword(user.getPassword());
            appUserStudentRepository.save(appUserStudent);
        }

        confirmationTokenService.setConfirmedAt(token);

        return ResponseEntity.ok().build();
    }

    private String buildEmail(String name, String link) {
        return "Hello " + name + ","
                + "\n\nPlease click on the following link to reset your password:"
                + "\n\n" + link;
    }

}
