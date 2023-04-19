package com.major.revalida.appuser.admin.crud.program;

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
@RequestMapping("/api/programs")
public class ProgramController {

	@Autowired
    private ProgramService programService;

    @GetMapping("/getAllPrograms")
    public List<Program> getAllPrograms() {
        return programService.getAllPrograms();
    }

    @GetMapping("/{programId}")
    public ResponseEntity<Program> getProgramById(@PathVariable Long programId) {
        Optional<Program> program = programService.getProgramById(programId);
        return program.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addProgram")
    public Program createProgram(@RequestBody Program program) {
        return programService.createProgram(program);
    }

    @PutMapping("/updateProgram/{programId}")
    public ResponseEntity<Program> updateProgram(@PathVariable Long programId, @RequestBody Program program) {
        Optional<Program> updatedProgram = programService.getProgramById(programId);
        if (updatedProgram.isPresent()) {
            return ResponseEntity.ok().body(programService.updateProgram(programId, program));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteProgram/{programId}")
    public ResponseEntity<HttpStatus> deleteProgram(@PathVariable Long programId) {
        Optional<Program> program = programService.getProgramById(programId);
        if (program.isPresent()) {
            programService.deleteProgram(programId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}
