package com.major.revalida.registration;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	private static final Pattern EMAIL_REGEX = Pattern.compile(
			"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	
	@Override
	public boolean test(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return EMAIL_REGEX.matcher(email).matches();
	}

}
