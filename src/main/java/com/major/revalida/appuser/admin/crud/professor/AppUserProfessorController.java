package com.major.revalida.appuser.admin.crud.professor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.major.revalida.appuser.AppUserRole;
import com.major.revalida.appuser.faculty.AppUserProfessor;
import com.major.revalida.appuser.faculty.AppUserProfessorRepository;
import com.major.revalida.login.user.AuthenticateService;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class AppUserProfessorController {

	private final AppUserProfessorRepository professorRepository;
	private final RegisterProfessorService registerProfessorService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllProfessor")
	public List<AppUserProfessor> getAllProfessors() {
		return professorRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserProfessor> getProfessorById(@PathVariable Long id) {
		Optional<AppUserProfessor> professor = professorRepository.findById(id);
		return professor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addProfessor")
	public ResponseEntity<AppUserProfessor> createProfessor(@RequestBody AppUserProfessor professor) {
		AppUserProfessor savedProfessor = registerProfessorService.createProfessor(professor);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProfessor);
	}

	@PutMapping("/updateProfessor/{id}")
	public ResponseEntity<AppUserProfessor> updateProfessor(@PathVariable Long id, @RequestBody AppUserProfessor professor) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(professor.getPassword());
		
		Optional<AppUserProfessor> existingProfessor = professorRepository.findById(id);
		if (existingProfessor.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update professor details
		existingProfessor.get().setFirstName(professor.getFirstName());
		existingProfessor.get().setMiddleName(professor.getMiddleName());
		existingProfessor.get().setLastName(professor.getLastName());
		existingProfessor.get().setWork(professor.getWork());
		existingProfessor.get().setGender(professor.getGender());
		existingProfessor.get().setStatus(professor.getStatus());
		existingProfessor.get().setBirthdate(professor.getBirthdate());
		existingProfessor.get().setPassword(encodedPassword);
		existingProfessor.get().setCollege(professor.getCollege());
		existingProfessor.get().setLocked(professor.getLocked());
		existingProfessor.get().setEnabled(professor.getEnabled());
		existingProfessor.get().setPhoto(professor.getPhoto());
		AppUserProfessor savedProfessor = professorRepository.save(existingProfessor.get());
		
		User user = userRepository.findByEmail(existingProfessor.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + professor.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(professor.getProfessorNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedProfessor);
	}
	
	@PutMapping("/enableProfessor/{id}")
	public ResponseEntity<AppUserProfessor> enableProfessor(@PathVariable Long id, @RequestBody AppUserProfessor professor) {
		
		Optional<AppUserProfessor> existingProfessor = professorRepository.findById(id);
		if (existingProfessor.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update professor details
		existingProfessor.get().setEnabled(professor.getEnabled());
		AppUserProfessor savedProfessor = professorRepository.save(existingProfessor.get());
		
		return ResponseEntity.ok(savedProfessor);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerProfessorService.updateProfessorNumber(email);
    }
	
}
