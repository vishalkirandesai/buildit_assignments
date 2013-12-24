package biz.buildit.util;

import biz.buildit.main.Plant;
import biz.buildit.rest.PlantResource;

public class PlantResourceAssembler {

	public static PlantResource getResource(Plant plant){
		PlantResource plantResource = new PlantResource();
		plantResource.setPId(plant.getPId());
		plantResource.setCostPerDay(plant.getPrice());
		plantResource.setDescription(plant.getDescription());
		plantResource.setName(plant.getName());
		if(plant.getStartDate() != null)
			plantResource.setStartDate(plant.getStartDate().getTime());
		if(plant.getEndDate() != null)
		plantResource.setEndDate(plant.getEndDate().getTime());
		return plantResource;
	}
}
