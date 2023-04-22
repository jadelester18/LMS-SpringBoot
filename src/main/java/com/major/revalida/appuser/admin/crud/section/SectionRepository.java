package com.major.revalida.appuser.admin.crud.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long>{

	Section findFirstByOrderBySectionName();

}
