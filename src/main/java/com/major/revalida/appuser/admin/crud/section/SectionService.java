package com.major.revalida.appuser.admin.crud.section;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
	
	@Autowired
    private SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Optional<Section> getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId);
    }

    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    public Section updateSection(Long sectionId, Section section) {
        section.setSectionId(sectionId);
        return sectionRepository.save(section);
    }

    public void deleteSection(Long sectionId) {
        sectionRepository.deleteById(sectionId);
    }
    
}
