package com.major.revalida.appuser.admin.crud.admission;

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

import com.major.revalida.appuser.admission.AppUserAdmission;
import com.major.revalida.appuser.admission.AppUserAdmissionRepository;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admission")
@RequiredArgsConstructor
public class AppUserAdmissionController {

	private final AppUserAdmissionRepository admissionRepository;
	private final RegisterAdmissionService registerAdmissionService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllAdmission")
	public List<AppUserAdmission> getAllAdmissions() {
		return admissionRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserAdmission> getAdmissionById(@PathVariable Long id) {
		Optional<AppUserAdmission> admission = admissionRepository.findById(id);
		return admission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addAdmission")
	public ResponseEntity<AppUserAdmission> createAdmission(@RequestBody AppUserAdmission admission) {
		AppUserAdmission savedAdmission = registerAdmissionService.createAdmission(admission);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmission);
	}

	@PutMapping("/updateAdmission/{id}")
	public ResponseEntity<AppUserAdmission> updateAdmission(@PathVariable Long id, @RequestBody AppUserAdmission admission) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(admission.getPassword());
		
		Optional<AppUserAdmission> existingAdmission = admissionRepository.findById(id);
		if (existingAdmission.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update admission details
		existingAdmission.get().setFirstName(admission.getFirstName());
		existingAdmission.get().setMiddleName(admission.getMiddleName());
		existingAdmission.get().setLastName(admission.getLastName());
		existingAdmission.get().setBirthdate(admission.getBirthdate());
		existingAdmission.get().setPassword(encodedPassword);
		existingAdmission.get().setLocked(admission.getLocked());
		existingAdmission.get().setEnabled(admission.getEnabled());
		existingAdmission.get().setPhoto(admission.getPhoto());
		AppUserAdmission savedAdmission = admissionRepository.save(existingAdmission.get());
		
		User user = userRepository.findByEmail(existingAdmission.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + admission.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(admission.getAdmissionNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedAdmission);
	}
	
	@PutMapping("/enableAdmission/{id}")
	public ResponseEntity<AppUserAdmission> enableAdmission(@PathVariable Long id, @RequestBody AppUserAdmission admission) {
		
		Optional<AppUserAdmission> existingAdmission = admissionRepository.findById(id);
		if (existingAdmission.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update admission details
		existingAdmission.get().setEnabled(admission.getEnabled());
		AppUserAdmission savedAdmission = admissionRepository.save(existingAdmission.get());
		
		return ResponseEntity.ok(savedAdmission);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerAdmissionService.updateAdmissionNumber(email);
    }
	
}
