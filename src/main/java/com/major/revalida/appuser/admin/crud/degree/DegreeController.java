package com.major.revalida.appuser.admin.crud.degree;

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
@RequestMapping("/api/degrees")
public class DegreeController {

	@Autowired
    private DegreeService degreeService;

    @GetMapping("/getAllDegrees")
    public List<Degree> getAllDegrees() {
        return degreeService.getAllDegrees();
    }

    @GetMapping("/{degreeId}")
    public ResponseEntity<Degree> getDegreeById(@PathVariable Long degreeId) {
        Optional<Degree> degree = degreeService.getDegreeById(degreeId);
        return degree.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addDegree")
    public Degree createDegree(@RequestBody Degree degree) {
        return degreeService.createDegree(degree);
    }

    @PutMapping("/updateDegree/{degreeId}")
    public ResponseEntity<Degree> updateDegree(@PathVariable Long degreeId, @RequestBody Degree degree) {
        Optional<Degree> updatedDegree = degreeService.getDegreeById(degreeId);
        if (updatedDegree.isPresent()) {
            return ResponseEntity.ok().body(degreeService.updateDegree(degreeId, degree));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteDegree/{degreeId}")
    public ResponseEntity<HttpStatus> deleteDegree(@PathVariable Long degreeId) {
        Optional<Degree> degree = degreeService.getDegreeById(degreeId);
        if (degree.isPresent()) {
            degreeService.deleteDegree(degreeId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
