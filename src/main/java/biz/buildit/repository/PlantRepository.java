package biz.buildit.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import biz.buildit.main.Plant;

@RooJpaRepository(domainType = Plant.class)
public interface PlantRepository {
	
	@Query("DELETE FROM Plant p")
	void removeAllEntries();
	
	@Query("SELECT p from Plant AS p WHERE 	lower(p.name) like lower(:name)")
	List<Plant> getByName(@Param("name") String name);
	
	@Query("SELECT p.id FROM Plant p")
	List<Long> getPlantIDs();
}
