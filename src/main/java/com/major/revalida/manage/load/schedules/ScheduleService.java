package com.major.revalida.manage.load.schedules;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.major.revalida.appuser.admin.crud.program.Program;
import com.major.revalida.appuser.admin.crud.room.Room;
import com.major.revalida.appuser.admin.crud.section.Section;
import com.major.revalida.appuser.admin.crud.subject.Subject;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ScheduleService {
	
	@Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule createSchedule(Schedule schedule) throws ScheduleConflictException {
        if (isScheduleConflict(schedule)) {
            throw new ScheduleConflictException("Schedule conflicts with existing schedules.");
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule schedule) throws ScheduleConflictException {
        Schedule existingSchedule = scheduleRepository.findById(id).orElse(null);
        if (existingSchedule != null) {
            // Check if the updated schedule conflicts with existing schedules
            if (isScheduleConflict(schedule, existingSchedule)) {
                throw new ScheduleConflictException("Schedule conflicts with existing schedules.");
            }
            // Update the existing schedule
            existingSchedule.setCurriculum(schedule.getCurriculum());
            existingSchedule.setSchoolYear(schedule.getSchoolYear());
            existingSchedule.setYearLevel(schedule.getYearLevel());
            existingSchedule.setSemester(schedule.getSemester());
            existingSchedule.setDay(schedule.getDay());
            existingSchedule.setStartTime(schedule.getStartTime());
            existingSchedule.setEndTime(schedule.getEndTime());
            existingSchedule.setCollege(schedule.getCollege());
            existingSchedule.setProgram(schedule.getProgram());
            existingSchedule.setSection(schedule.getSection());
            existingSchedule.setSubject(schedule.getSubject());
            existingSchedule.setRoom(schedule.getRoom());
            existingSchedule.setProfessor(schedule.getProfessor());
            return scheduleRepository.save(existingSchedule);
        } else {
            return null;
        }
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    private boolean isScheduleConflict(Schedule schedule) {
        List<Schedule> schedules = scheduleRepository.findByRoomAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                schedule.getRoom(),
                schedule.getDay(),
                schedule.getEndTime(),
        		schedule.getStartTime());
        for (Schedule s : schedules) {
            if (s.getSchoolYear().equals(schedule.getSchoolYear())
                    && s.getYearLevel().equals(schedule.getYearLevel())
                    && s.getSemester().equals(schedule.getSemester())
                    && (s.getScheduleId() != null && !s.getScheduleId().equals(schedule.getScheduleId()))) {
                return true;
            }
        }
        return false;
    }

    private boolean isScheduleConflict(Schedule schedule, Schedule existingSchedule) {
        List<Schedule> schedules = scheduleRepository.findByRoomAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                schedule.getRoom(),
                schedule.getDay(),
                schedule.getEndTime(),
        		schedule.getStartTime());
        for (Schedule s : schedules) {
            if (s.getSchoolYear().equals(schedule.getSchoolYear())
                    && s.getYearLevel().equals(schedule.getYearLevel())
                    && s.getSemester().equals(schedule.getSemester())
                    && (s.getScheduleId() == null || !s.getScheduleId().equals(existingSchedule.getScheduleId()))) {
                return true;
            }
        }
        return false;
    }

    
}
