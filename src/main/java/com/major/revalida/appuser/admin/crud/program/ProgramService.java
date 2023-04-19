package com.major.revalida.appuser.admin.crud.program;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {
	
	@Autowired
    private ProgramRepository programRepository;
	
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static String generateRandomString(int length) {
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
        int randomIndex = RANDOM.nextInt(ALPHANUMERIC_CHARS.length());
        sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
      }
      return sb.toString();
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public Optional<Program> getProgramById(Long programId) {
        return programRepository.findById(programId);
    }

    public Program createProgram(Program program) {
    	program.setProgramCode(generateRandomString(5));
        return programRepository.save(program);
    }

    public Program updateProgram(Long programId, Program program) {
        program.setProgramId(programId);
        return programRepository.save(program);
    }

    public void deleteProgram(Long programId) {
        programRepository.deleteById(programId);
    }
    
}
