package com.major.revalida.admission.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.major.revalida.appuser.student.AppUserStudent;

@RestController
@RequestMapping("/admission/students")
public class AppUserStudentController {

    @Autowired
    private AppUserStudentAdmissionService studentService;

    @GetMapping("/getall")
    public List<AppUserStudent> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public AppUserStudent getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable Long id, @RequestBody AppUserStudent updatedStudent) {
        studentService.updateStudent(id, updatedStudent);
    }

    @PutMapping("/generate-user-no/{email}")
    public void updateUserNo(@PathVariable String email) {
        studentService.updateStudentNumber(email);
    }
    
}
