package com.major.revalida.appuser.admin.crud.dean;

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

import com.major.revalida.appuser.dean.AppUserDean;
import com.major.revalida.appuser.dean.AppUserDeanRepository;
import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dean")
@RequiredArgsConstructor
public class AppUserDeanController {

	private final AppUserDeanRepository deanRepository;
	private final RegisterDeanService registerDeanService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/getAllDean")
	public List<AppUserDean> getAllDeans() {
		return deanRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserDean> getDeanById(@PathVariable Long id) {
		Optional<AppUserDean> dean = deanRepository.findById(id);
		return dean.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addDean")
	public ResponseEntity<AppUserDean> createDean(@RequestBody AppUserDean dean) {
		AppUserDean savedDean = registerDeanService.createDean(dean);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedDean);
	}

	@PutMapping("/updateDean/{id}")
	public ResponseEntity<AppUserDean> updateDean(@PathVariable Long id, @RequestBody AppUserDean dean) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(dean.getPassword());
		
		Optional<AppUserDean> existingDean = deanRepository.findById(id);
		if (existingDean.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update dean details
		existingDean.get().setFirstName(dean.getFirstName());
		existingDean.get().setMiddleName(dean.getMiddleName());
		existingDean.get().setLastName(dean.getLastName());
		existingDean.get().setBirthdate(dean.getBirthdate());
		existingDean.get().setPassword(encodedPassword);
		existingDean.get().setCollege(dean.getCollege());
		existingDean.get().setLocked(dean.getLocked());
		existingDean.get().setEnabled(dean.getEnabled());
		existingDean.get().setPhoto(dean.getPhoto());
		AppUserDean savedDean = deanRepository.save(existingDean.get());
		
		User user = userRepository.findByEmail(existingDean.get().getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email " + dean.getEmail()));
		user.setPassword(encodedPassword);
		user.setUserNo(dean.getDeanNo());
		userRepository.save(user);
		
		return ResponseEntity.ok(savedDean);
	}
	
	@PutMapping("/enableDean/{id}")
	public ResponseEntity<AppUserDean> enableDean(@PathVariable Long id, @RequestBody AppUserDean dean) {
		
		Optional<AppUserDean> existingDean = deanRepository.findById(id);
		if (existingDean.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		// update dean details
		existingDean.get().setEnabled(dean.getEnabled());
		AppUserDean savedDean = deanRepository.save(existingDean.get());
		
		return ResponseEntity.ok(savedDean);
	}
	
    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
    	registerDeanService.updateDeanNumber(email);
    }
	
}
