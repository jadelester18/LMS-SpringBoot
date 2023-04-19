package com.major.revalida.appuser.admin.crud.subject;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.admin.crud.college.College;
import com.major.revalida.appuser.admin.crud.college.CollegeRepository;

@Service
public class SubjectService {

	@Autowired
    private SubjectRepository subjectRepository;
	
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

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId);
    }

    public Subject createSubject(Subject subject) {
    	subject.setSubjectCode(generateRandomString(5));
        return subjectRepository.save(subject);
    }

    public Subject updateSubject(Long subjectId, Subject subject) {
    	subject.setSubjectId(subjectId);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long subjectId) {
    	subjectRepository.deleteById(subjectId);
    }
	
}
