package com.major.revalida.appuser.admin.crud.curriculum;

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
@RequestMapping("/api/curriculums")
public class CurriculumController {

	 @Autowired
	    private CurriculumService curriculumService;

	    @GetMapping("/getAllCurriculums")
	    public List<Curriculum> findAll() {
	        return curriculumService.getAllCurriculums();
	    }

	    @GetMapping("/{curriculumId}")
	    public ResponseEntity<Curriculum> getCurriculumById(@PathVariable Long curriculumId) {
	        Optional<Curriculum> curriculum = curriculumService.getCurriculumById(curriculumId);
	        return curriculum.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    @PostMapping("/addCurriculum")
	    public Curriculum createCurriculum(@RequestBody Curriculum curriculum) {
	        return curriculumService.createCurriculum(curriculum);
	    }

	    @PutMapping("/updateCurriculum/{curriculumId}")
	    public ResponseEntity<Curriculum> updateCurriculum(@PathVariable Long curriculumId, @RequestBody Curriculum curriculum) {
	        Optional<Curriculum> updatedCurriculum = curriculumService.getCurriculumById(curriculumId);
	        if (updatedCurriculum.isPresent()) {
	            return ResponseEntity.ok().body(curriculumService.updateCurriculum(curriculumId, curriculum));
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/deleteCurriculum/{curriculumId}")
	    public ResponseEntity<HttpStatus> deleteCurriculum(@PathVariable Long curriculumId) {
	        Optional<Curriculum> curriculum = curriculumService.getCurriculumById(curriculumId);
	        if (curriculum.isPresent()) {
	        	curriculumService.deleteCurriculum(curriculumId);
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	
}
