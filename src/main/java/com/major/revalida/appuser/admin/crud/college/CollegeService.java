package com.major.revalida.appuser.admin.crud.college;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {
	
	@Autowired
    private CollegeRepository collegeRepository;

    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    public Optional<College> getCollegeById(Long collegeId) {
        return collegeRepository.findById(collegeId);
    }

    public College createCollege(College college) {
        return collegeRepository.save(college);
    }

    public College updateCollege(Long collegeId, College college) {
        college.setCollegeId(collegeId);
        return collegeRepository.save(college);
    }

    public void deleteCollege(Long collegeId) {
        collegeRepository.deleteById(collegeId);
    }
    
}
