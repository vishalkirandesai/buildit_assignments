package biz.buildit.repository;
import biz.buildit.main.SiteEngineer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = SiteEngineer.class)
public interface SiteEngineerRepository {
	
	@Query("SELECT s FROM Users s WHERE s.username like :name")
	@Transactional(readOnly=true)
	public SiteEngineer findSiteEngineerByName(@Param("name")String name);
}
