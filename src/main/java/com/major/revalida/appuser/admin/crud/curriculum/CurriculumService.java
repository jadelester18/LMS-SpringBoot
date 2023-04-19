package com.major.revalida.appuser.admin.crud.curriculum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurriculumService {

	@Autowired
    private CurriculumRepository curriculumRepository;

    public List<Curriculum> getAllCurriculums() {
        return curriculumRepository.findAll();
    }

    public Optional<Curriculum> getCurriculumById(Long curriculumId) {
        return curriculumRepository.findById(curriculumId);
    }

    public Curriculum createCurriculum(Curriculum curriculum) {
    	curriculum.setCurriculumCreatedYear(LocalDate.now());
        return curriculumRepository.save(curriculum);
    }

    public Curriculum updateCurriculum(Long curriculumId, Curriculum curriculum) 	{
    	Optional<Curriculum> existingCurriculum = curriculumRepository.findById(curriculumId);
        if (existingCurriculum.isPresent()) {
            curriculum.setCurriculumId(curriculumId);
            return curriculumRepository.save(curriculum);
        } else {
            return null;
        }
    }

    public void deleteCurriculum(Long curriculumId) {
    	curriculumRepository.deleteById(curriculumId);
    }
	
}
