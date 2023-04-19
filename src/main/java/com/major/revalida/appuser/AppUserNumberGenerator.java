package com.major.revalida.appuser;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.major.revalida.login.user.User;
import com.major.revalida.login.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserNumberGenerator {

	private final UserRepository userRepository;
	
	public String generateUserNumber() {
        String userNumber = null;
        // Get the last two digits of the current year
        int year = LocalDate.now().getYear() % 100;
        // Get the current month with zero-padding
        String month = String.format("%02d", LocalDate.now().getMonthValue());

        // Try to generate a unique user number
        boolean unique = false;
        while (!unique) {
            // Get the current count of students with this year and month prefix
            int count = userRepository.countByUserNoStartingWith(year + month);
            // Increment the count and format with zero-padding
            String countStr = String.format("%08d", count + 1);
            // Concatenate the year, month, and count to form the student number
            userNumber = year + month + countStr;

            // Check if the user number already exists in the database
            Optional<User> existingUser = userRepository.findByUserNo(userNumber);
            if (existingUser.isEmpty()) {
                unique = true;
            }
        }

        return userNumber;
    }
	
}
