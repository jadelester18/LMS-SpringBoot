package com.major.revalida.manage.load.schedules;

import java.util.List;

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
@RequestMapping("/api/schedules")
public class ScheduleController {

	 @Autowired
	    private ScheduleService scheduleService;

	    @GetMapping("/getAllSchedule")
	    public ResponseEntity<List<Schedule>> getAllSchedules() {
	        List<Schedule> schedules = scheduleService.getAllSchedules();
	        return new ResponseEntity<>(schedules, HttpStatus.OK);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
	        Schedule schedule = scheduleService.getScheduleById(id);
	        if (schedule != null) {
	            return new ResponseEntity<>(schedule, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @PostMapping("/addSchedule")
	    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) throws ScheduleConflictException {
	        Schedule createdSchedule = scheduleService.createSchedule(schedule);
	        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
	    }

	    @PutMapping("/updateSchedule/{id}")
	    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) throws ScheduleConflictException {
	        Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
	        if (updatedSchedule != null) {
	            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("/deleteSchedule/{id}")
	    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
	        scheduleService.deleteSchedule(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    
}
