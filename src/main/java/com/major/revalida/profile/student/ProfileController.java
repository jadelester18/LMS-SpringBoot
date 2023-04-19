package com.major.revalida.profile.student;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.login.user.AuthenticateService;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/profile/open")
@RequiredArgsConstructor
public class ProfileController {

	private final AppUserStudentRepository appUserStudentRepository;
	private final UserRepository userRepository;
    
    @GetMapping("/view-profile")
    public ResponseEntity<AppUserStudent> viewProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        AppUserStudent appUserStudent = appUserStudentRepository.findByStudentNo(userDetails.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return ResponseEntity.ok(appUserStudent);
    }

    @PutMapping("/profile/{studentId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long studentId, @RequestBody AppUserStudent updatedUser) {
        Optional<AppUserStudent> optionalUser = appUserStudentRepository.findById(studentId);

        if (optionalUser.isPresent()) {
            AppUserStudent user = optionalUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setMiddleName(updatedUser.getMiddleName());
            user.setLastName(updatedUser.getLastName());
//            user.setEmail(updatedUser.getEmail());
//            user.setPhotos(updatedUser.getPhotos());

            appUserStudentRepository.save(user);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
