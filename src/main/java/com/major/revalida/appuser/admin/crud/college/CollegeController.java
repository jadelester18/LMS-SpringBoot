package com.major.revalida.appuser.admin.crud.college;

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
@RequestMapping("/api/colleges")
public class CollegeController {

	@Autowired
    private CollegeService collegeService;

    @GetMapping("/getAllColleges")
    public List<College> getAllColleges() {
        return collegeService.getAllColleges();
    }

    @GetMapping("/{collegeId}")
    public ResponseEntity<College> getCollegeById(@PathVariable Long collegeId) {
        Optional<College> college = collegeService.getCollegeById(collegeId);
        return college.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addCollege")
    public College createCollege(@RequestBody College college) {
        return collegeService.createCollege(college);
    }

    @PutMapping("/updateCollege/{collegeId}")
    public ResponseEntity<College> updateCollege(@PathVariable Long collegeId, @RequestBody College college) {
        Optional<College> updatedCollege = collegeService.getCollegeById(collegeId);
        if (updatedCollege.isPresent()) {
            return ResponseEntity.ok().body(collegeService.updateCollege(collegeId, college));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteCollege/{collegeId}")
    public ResponseEntity<HttpStatus> deleteCollege(@PathVariable Long collegeId) {
        Optional<College> college = collegeService.getCollegeById(collegeId);
        if (college.isPresent()) {
            collegeService.deleteCollege(collegeId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
