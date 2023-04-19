package com.major.revalida.manage.load.schedules;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.major.revalida.appuser.admin.crud.program.Program;
import com.major.revalida.appuser.admin.crud.room.Room;
import com.major.revalida.appuser.admin.crud.section.Section;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{

	List<Schedule> findByRoomAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Room room, DayOfWeek day,
			LocalTime endTime, LocalTime startTime);

	List<Schedule> findByProgramAndSection(Program program, Section section);


}
