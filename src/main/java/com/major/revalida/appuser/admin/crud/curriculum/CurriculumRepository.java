package com.major.revalida.appuser.admin.crud.curriculum;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.major.revalida.manage.load.schedules.Schedule;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long>  {
	
	Optional<Curriculum> findByCurriculumId(Long id);
	
}
