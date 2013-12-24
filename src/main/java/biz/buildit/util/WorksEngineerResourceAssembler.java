package biz.buildit.util;

import biz.buildit.main.WorksEngineer;
import biz.buildit.rest.WorksEngineerResource;

public class WorksEngineerResourceAssembler {

	public static WorksEngineerResource getResource(WorksEngineer worksEngineer){
		WorksEngineerResource worksEngineerResource = new WorksEngineerResource();
		worksEngineerResource.setId(worksEngineer.getId());
		worksEngineerResource.setName(worksEngineer.getUsername());
		return worksEngineerResource;
	}
}
