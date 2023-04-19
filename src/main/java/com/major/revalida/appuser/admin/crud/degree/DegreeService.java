package com.major.revalida.appuser.admin.crud.degree;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {
	
	@Autowired
    private DegreeRepository degreeRepository;

    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    public Optional<Degree> getDegreeById(Long degreeId) {
        return degreeRepository.findById(degreeId);
    }

    public Degree createDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    public Degree updateDegree(Long degreeId, Degree degree) {
        degree.setDegreeId(degreeId);
        return degreeRepository.save(degree);
    }

    public void deleteDegree(Long degreeId) {
        degreeRepository.deleteById(degreeId);
    }
    
}
