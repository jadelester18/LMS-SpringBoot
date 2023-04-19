package com.major.revalida.appuser.admin.crud.subject;

import java.time.LocalTime;
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

import com.major.revalida.appuser.admin.crud.college.College;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

	 @Autowired
	    private SubjectService subjectService;

	    @GetMapping("/getAllSubjects")
	    public List<Subject> findAll() {
	        return subjectService.getAllSubjects();
	    }

	    @GetMapping("/{subjectId}")
	    public ResponseEntity<Subject> getSubjectById(@PathVariable Long subjectId) {
	        Optional<Subject> subject = subjectService.getSubjectById(subjectId);
	        return subject.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    @PostMapping("/addSubject")
	    public Subject createSubject(@RequestBody Subject subject) {
	    	
	    	 // Parse lengthOfDiscussionsHour and lengthOfDiscussionsMinute into a LocalTime object
	        LocalTime lengthOfDiscussions = LocalTime.of(Integer.parseInt(subject.getLengthOfDiscussionsHour()), Integer.parseInt(subject.getLengthOfDiscussionsMinute()));
	        // Set the LocalTime object to the Subject entity
	        subject.setLengthOfDiscussions(lengthOfDiscussions);
	    	
	        return subjectService.createSubject(subject);
	    }

	    @PutMapping("/updateSubject/{subjectId}")
	    public ResponseEntity<Subject> updateSubject(@PathVariable Long subjectId, @RequestBody Subject subject) {
	        Optional<Subject> updatedSubject = subjectService.getSubjectById(subjectId);
	        if (updatedSubject.isPresent()) {
	            return ResponseEntity.ok().body(subjectService.updateSubject(subjectId, subject));
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/deleteSubject/{subjectId}")
	    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable Long subjectId) {
	        Optional<Subject> subject = subjectService.getSubjectById(subjectId);
	        if (subject.isPresent()) {
	        	subjectService.deleteSubject(subjectId);
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	
}
