package com.major.revalida.appuser.admin.crud.section;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

	@Autowired
    private SectionService sectionService;

    @GetMapping("/getAllsections")
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long sectionId) {
        Optional<Section> section = sectionService.getSectionById(sectionId);
        return section.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addSection")
    public Section createSection(@RequestBody Section section) {
        return sectionService.createSection(section);
    }

    @PutMapping("/updateSection/{sectionId}")
    public ResponseEntity<Section> updateSection(@PathVariable Long sectionId, @RequestBody Section section) {
        Optional<Section> updatedSection = sectionService.getSectionById(sectionId);
        if (updatedSection.isPresent()) {
            return ResponseEntity.ok().body(sectionService.updateSection(sectionId, section));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteSection/{sectionId}")
    public ResponseEntity<HttpStatus> deleteSection(@PathVariable Long sectionId) {
        Optional<Section> section = sectionService.getSectionById(sectionId);
        if (section.isPresent()) {
            sectionService.deleteSection(sectionId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
