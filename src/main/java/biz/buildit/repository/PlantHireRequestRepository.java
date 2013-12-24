package biz.buildit.repository;
import biz.buildit.main.PlantHireRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = PlantHireRequest.class)
public interface PlantHireRequestRepository {
	
	@Query("DELETE FROM PlantHireRequest p WHERE p.id = :id")
	@Transactional(readOnly=false)
	void cancelPlantHireRequest(@Param("id") int id);
	
	@Query("SELECT p FROM PlantHireRequest p WHERE p.id = :id")
	@Transactional(readOnly=false)
	PlantHireRequest getPlantHireRequest(@Param("id") int id);
}
