package com.major.revalida.appuser.admin.crud.admin;

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

import com.major.revalida.appuser.admin.AppUserAdmin;
import com.major.revalida.appuser.admin.AppUserAdminRepository;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AppUserAdminController {

	private final AppUserAdminRepository admissionRepository;
	private final RegisterAdminService registerAdminService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllAdmin")
	public List<AppUserAdmin> getAllAdmins() {
		return admissionRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserAdmin> getAdminById(@PathVariable Long id) {
		Optional<AppUserAdmin> admission = admissionRepository.findById(id);
		return admission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addAdmin")
	public ResponseEntity<AppUserAdmin> createAdmin(@RequestBody AppUserAdmin admission) {
		AppUserAdmin savedAdmin = registerAdminService.createAdmin(admission);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
	}

	@PutMapping("/updateAdmin/{id}")
	public ResponseEntity<AppUserAdmin> updateAdmin(@PathVariable Long id, @RequestBody AppUserAdmin admission) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(admission.getPassword());
		
		Optional<AppUserAdmin> existingAdmin = admissionRepository.findById(id);
		if (existingAdmin.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update admission details
		existingAdmin.get().setFirstName(admission.getFirstName());
		existingAdmin.get().setMiddleName(admission.getMiddleName());
		existingAdmin.get().setLastName(admission.getLastName());
		existingAdmin.get().setBirthdate(admission.getBirthdate());
		existingAdmin.get().setPassword(encodedPassword);
		existingAdmin.get().setLocked(admission.getLocked());
		existingAdmin.get().setEnabled(admission.getEnabled());
		existingAdmin.get().setPhoto(admission.getPhoto());
		AppUserAdmin savedAdmin = admissionRepository.save(existingAdmin.get());
		
		User user = userRepository.findByEmail(existingAdmin.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + admission.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(admission.getAdminNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedAdmin);
	}
	
	@PutMapping("/enableAdmin/{id}")
	public ResponseEntity<AppUserAdmin> enableAdmin(@PathVariable Long id, @RequestBody AppUserAdmin admission) {
		
		Optional<AppUserAdmin> existingAdmin = admissionRepository.findById(id);
		if (existingAdmin.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update admission details
		existingAdmin.get().setEnabled(admission.getEnabled());
		AppUserAdmin savedAdmin = admissionRepository.save(existingAdmin.get());
		
		return ResponseEntity.ok(savedAdmin);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerAdminService.updateAdminNumber(email);
    }
	
}
