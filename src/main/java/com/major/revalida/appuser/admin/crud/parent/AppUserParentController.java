package com.major.revalida.appuser.admin.crud.parent;

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

import com.major.revalida.appuser.parent.AppUserParent;
import com.major.revalida.appuser.parent.AppUserParentRepository;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class AppUserParentController {

	private final AppUserParentRepository parentRepository;
	private final RegisterParentService registerParentService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllParent")
	public List<AppUserParent> getAllParents() {
		return parentRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserParent> getParentById(@PathVariable Long id) {
		Optional<AppUserParent> parent = parentRepository.findById(id);
		return parent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addParent")
	public ResponseEntity<AppUserParent> createParent(@RequestBody AppUserParent parent) {
		AppUserParent savedParent = registerParentService.createParent(parent);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedParent);
	}

	@PutMapping("/updateParent/{id}")
	public ResponseEntity<AppUserParent> updateParent(@PathVariable Long id, @RequestBody AppUserParent parent) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(parent.getPassword());
		
		Optional<AppUserParent> existingParent = parentRepository.findById(id);
		if (existingParent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update parent details
		existingParent.get().setFirstName(parent.getFirstName());
		existingParent.get().setMiddleName(parent.getMiddleName());
		existingParent.get().setLastName(parent.getLastName());
		existingParent.get().setBirthdate(parent.getBirthdate());
		existingParent.get().setPassword(encodedPassword);
		existingParent.get().setLocked(parent.getLocked());
		existingParent.get().setEnabled(parent.getEnabled());
		existingParent.get().setPhoto(parent.getPhoto());
		AppUserParent savedParent = parentRepository.save(existingParent.get());
		
		User user = userRepository.findByEmail(existingParent.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + parent.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(parent.getParentNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedParent);
	}
	
	@PutMapping("/enableParent/{id}")
	public ResponseEntity<AppUserParent> enableParent(@PathVariable Long id, @RequestBody AppUserParent parent) {
		
		Optional<AppUserParent> existingParent = parentRepository.findById(id);
		if (existingParent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update parent details
		existingParent.get().setEnabled(parent.getEnabled());
		AppUserParent savedParent = parentRepository.save(existingParent.get());
		
		return ResponseEntity.ok(savedParent);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerParentService.updateParentNumber(email);
    }
	
}
