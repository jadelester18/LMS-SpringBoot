package com.major.revalida.registration;

import java.time.LocalDate;

import com.major.revalida.appuser.admin.crud.program.Program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
	
	private String email;
	private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthdate;
    private Program program;
	
}
