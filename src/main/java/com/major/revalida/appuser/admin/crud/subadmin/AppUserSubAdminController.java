package com.major.revalida.appuser.admin.crud.subadmin;

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

import com.major.revalida.appuser.subadmin.AppUserSubAdmin;
import com.major.revalida.appuser.subadmin.AppUserSubAdminRepository;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subadmin")
@RequiredArgsConstructor
public class AppUserSubAdminController {

	private final AppUserSubAdminRepository subadminRepository;
	private final RegisterSubAdminService registerSubAdminService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllSubAdmin")
	public List<AppUserSubAdmin> getAllSubAdmins() {
		return subadminRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserSubAdmin> getSubAdminById(@PathVariable Long id) {
		Optional<AppUserSubAdmin> subadmin = subadminRepository.findById(id);
		return subadmin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addSubAdmin")
	public ResponseEntity<AppUserSubAdmin> createSubAdmin(@RequestBody AppUserSubAdmin subadmin) {
		AppUserSubAdmin savedSubAdmin = registerSubAdminService.createSubAdmin(subadmin);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedSubAdmin);
	}

	@PutMapping("/updateSubAdmin/{id}")
	public ResponseEntity<AppUserSubAdmin> updateSubAdmin(@PathVariable Long id, @RequestBody AppUserSubAdmin subadmin) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(subadmin.getPassword());
		
		Optional<AppUserSubAdmin> existingSubAdmin = subadminRepository.findById(id);
		if (existingSubAdmin.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update subadmin details
		existingSubAdmin.get().setFirstName(subadmin.getFirstName());
		existingSubAdmin.get().setMiddleName(subadmin.getMiddleName());
		existingSubAdmin.get().setLastName(subadmin.getLastName());
		existingSubAdmin.get().setBirthdate(subadmin.getBirthdate());
		existingSubAdmin.get().setPassword(encodedPassword);
		existingSubAdmin.get().setLocked(subadmin.getLocked());
		existingSubAdmin.get().setEnabled(subadmin.getEnabled());
		existingSubAdmin.get().setPhoto(subadmin.getPhoto());
		AppUserSubAdmin savedSubAdmin = subadminRepository.save(existingSubAdmin.get());
		
		User user = userRepository.findByEmail(existingSubAdmin.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + subadmin.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(subadmin.getSubAdminNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedSubAdmin);
	}
	
	@PutMapping("/enableSubAdmin/{id}")
	public ResponseEntity<AppUserSubAdmin> enableSubAdmin(@PathVariable Long id, @RequestBody AppUserSubAdmin subadmin) {
		
		Optional<AppUserSubAdmin> existingSubAdmin = subadminRepository.findById(id);
		if (existingSubAdmin.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update subadmin details
		existingSubAdmin.get().setEnabled(subadmin.getEnabled());
		AppUserSubAdmin savedSubAdmin = subadminRepository.save(existingSubAdmin.get());
		
		return ResponseEntity.ok(savedSubAdmin);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerSubAdminService.updateSubAdminNumber(email);
    }
	
}
