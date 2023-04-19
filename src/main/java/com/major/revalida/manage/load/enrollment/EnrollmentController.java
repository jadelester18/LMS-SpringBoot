package com.major.revalida.manage.load.enrollment;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.major.revalida.appuser.admin.crud.subject.Subject;
import com.major.revalida.appuser.admin.crud.subject.SubjectRepository;
import com.major.revalida.appuser.student.AppUserStudent;
import com.major.revalida.appuser.student.AppUserStudentRepository;
import com.major.revalida.manage.load.schedules.Schedule;
import com.major.revalida.manage.load.schedules.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
	
	private final AppUserStudentRepository appUserStudentRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final ScheduleRepository scheduleRepository;
	private final SubjectRepository subjectRepository ;

	@GetMapping("/available-subjects")
    public ResponseEntity<List<SubjectDto>> getEnrollableSubjectsByProgram() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<AppUserStudent> optionalStudent = appUserStudentRepository.findByStudentNo(userDetails.getUsername());
        
        if (optionalStudent.isPresent()) {
            
            AppUserStudent student = optionalStudent.get();
            
            // Retrieve the list of subjects that the student can enroll in based on their program and section
            List<Schedule> scheduleList = scheduleRepository.findByProgramAndSection(
                    student.getProgram(),
                    student.getSection()
            );
            
            // Map the retrieved data to a DTO class containing only the required information
            List<SubjectDto> subjectDtoList = scheduleList.stream()
                    .map(schedule -> new SubjectDto(
                    		schedule.getScheduleId(),
                    		schedule.getCurriculum().getCurriculumName(),
                    		schedule.getCollege().getCollegeName(),
                    		schedule.getProgram().getProgramTitle(),
                    		schedule.getSection().getSectionName(),
                    		schedule.getYearLevel(),
                    		schedule.getSemester(),
                            schedule.getDay(),
                            schedule.getStartTime(),
                            schedule.getEndTime(),
                            schedule.getRoom().getRoomName(),
                            schedule.getSubject().getSubjectCode(),
                            schedule.getSubject().getSubjectTitle(),
                            schedule.getSubject().getUnits(),
                            schedule.getSubject().getPrerequisiteSubject(),	
                            schedule.getProfessor() != null ? schedule.getProfessor().getFirstName() : null,
                            schedule.getProfessor() != null ? schedule.getProfessor().getLastName() : null,
                            false
                    ))
                    .collect(Collectors.toList());
            
            // Return the DTO class as a response entity with a success status code
            return ResponseEntity.ok(subjectDtoList);
            
        } else {
            
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
            
        }
    }

	@PostMapping("/enroll")
	public ResponseEntity<?> enrollSubjects(@RequestBody List<Long> scheduleIds) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Optional<AppUserStudent> optionalStudent = appUserStudentRepository.findByStudentNo(userDetails.getUsername());

	    if (optionalStudent.isPresent()) {
	        AppUserStudent student = optionalStudent.get();
	        List<Schedule> scheduleList = scheduleRepository.findAllById(scheduleIds);

	        for (Schedule schedule : scheduleList) {
	            // Check if the student is already enrolled in the selected subject schedule
	            if (enrollmentRepository.existsByStudentAndSchedule(student, schedule)) {
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is already enrolled in the selected subject schedule");
	            }
	            

	            // Check if the student has finished the prerequisites of the selected subject schedule
	            if (schedule.getSubject().getPrerequisiteSubject() != null) {
	                Subject prerequisiteSubject = schedule.getSubject().getPrerequisiteSubject();
	                if (!enrollmentRepository.existsByStudentAndSubject(student, prerequisiteSubject)) {
	                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student has not finished the prerequisites of the selected subject schedule");
	                }
	            }

	            // Check if the student has already taken the selected subject schedule
	            if (enrollmentRepository.existsByStudentAndSubject(student, schedule.getSubject())) {
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student has already taken the selected subject schedule");
	            }
	            //Note : If the student already enrolled to this subject but failed allow again to enroll to this subject

	            // Create the enrollment
	            Enrollment enrollment = new Enrollment(student, schedule, LocalDate.now(), schedule.getSubject());
	            enrollmentRepository.save(enrollment);
	        }

	        // Update the student's academic progress
	        int enrolledSemester = scheduleList.get(0).getSemester();
	        int enrolledYearLevel = student.getYearLevel();

	        if (enrolledSemester == 2) {
	            enrolledYearLevel++;
	            if (enrolledYearLevel > 5) {
	                enrolledYearLevel = 1;
	            }
	        }

	        student.setSemester(enrolledSemester == 1 ? 2 : 1);
	        student.setYearLevel(enrolledYearLevel);
	        appUserStudentRepository.save(student);

	        // Return a success response
//	        return ResponseEntity.ok().build();
	        return ResponseEntity.ok("Enrollment successful");
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
	    }
	}

	@GetMapping("/enrolled-subjects")
	public ResponseEntity<List<EnrolledSubjectDto>> getEnrolledSubjects() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Optional<AppUserStudent> optionalStudent = appUserStudentRepository.findByStudentNo(userDetails.getUsername());

	    if (optionalStudent.isPresent()) {
	        AppUserStudent student = optionalStudent.get();
	        List<Enrollment> enrollmentList = enrollmentRepository.findByStudent(student);

	        List<EnrolledSubjectDto> enrolledSubjectDtoList = enrollmentList.stream()
	                .map(enrollment -> new EnrolledSubjectDto(
	                		enrollment.getId(),
	                		enrollment.getSubject().getSubjectCode(),
	                		enrollment.getSubject().getSubjectTitle(),
	                		enrollment.getSubject().getUnits(),
	                		enrollment.getSchedule().getCurriculum().getCurriculumName(),
	                		enrollment.getSchedule().getCollege().getCollegeName(),
	                		enrollment.getSchedule().getProgram().getProgramTitle(),
	                		enrollment.getSchedule().getSection().getSectionName(),
	                		enrollment.getSchedule().getYearLevel(),
	                		enrollment.getSchedule().getSemester(),
	                        enrollment.getSchedule().getDay(),
	                        enrollment.getSchedule().getStartTime(),
	                        enrollment.getSchedule().getEndTime(),
	                        enrollment.getSchedule().getRoom().getRoomName(),
	                        enrollment.getSchedule().getProfessor() != null ? enrollment.getSchedule().getProfessor().getFirstName() : null,
	                        enrollment.getSchedule().getProfessor() != null ? enrollment.getSchedule().getProfessor().getLastName() : null
	                ))
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(enrolledSubjectDtoList);
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
	    }
	}


	
}
