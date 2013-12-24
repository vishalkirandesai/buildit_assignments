package biz.buildit.repository;
import biz.buildit.main.WorksEngineer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = WorksEngineer.class)
public interface WorksEngineerRepository {
	
	@Query("SELECT w FROM WorksEngineer w where w.username like :name")
	@Transactional(readOnly=true)
	WorksEngineer findWorksEngineerByName(@Param("name") String name);
}
