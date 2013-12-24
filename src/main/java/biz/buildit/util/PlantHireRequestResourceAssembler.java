package biz.buildit.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import biz.buildit.main.PlantHireRequest;
import biz.buildit.rest.PlantHireRequestResource;
import biz.buildit.rest.PlantHireRequestResourceList;
import biz.buildit.rest.controller.PlantHireRequestRestController;

public class PlantHireRequestResourceAssembler extends ResourceAssemblerSupport<PlantHireRequest,PlantHireRequestResource>{

	public PlantHireRequestResourceAssembler(){
		super(PlantHireRequestRestController.class,PlantHireRequestResource.class);
	}
	public static PlantHireRequestResource getPlantHireRequestResource(PlantHireRequest plantHireRequest){
		PlantHireRequestResource plantHireRequestResource = new PlantHireRequestResource();
		plantHireRequestResource.setSiteEngineerResource(SiteEngineerResourceAssembler.getResource(plantHireRequest.getSiteEng()));
		plantHireRequestResource.setSiteId(plantHireRequest.getSiteId());
		plantHireRequestResource.setPlantResource(PlantResourceAssembler.getResource(plantHireRequest.getPlant()));
		plantHireRequestResource.setStartDate(plantHireRequest.getStartDate().getTime());
		plantHireRequestResource.setEndDate(plantHireRequest.getEndDate().getTime());
		if(plantHireRequest.getExtensionDate() != null)
			plantHireRequestResource.setExtensionDate(plantHireRequest.getExtensionDate().getTime());
		plantHireRequestResource.setWEngineerResource(WorksEngineerResourceAssembler.getResource(plantHireRequest.getWEng()));
		plantHireRequestResource.setApproval(plantHireRequest.getApproval().getStatusCode());
		plantHireRequestResource.setComments(plantHireRequest.getComments());
		return plantHireRequestResource;
	}
	
	public static PlantHireRequestResourceList getPlantHireRequestResourceList(List<PlantHireRequest> plantHireRequests){
		List<PlantHireRequestResource> plantHireRequestResources = new ArrayList<PlantHireRequestResource>();
		for(PlantHireRequest plantHireRequest:plantHireRequests){
			plantHireRequestResources.add(getPlantHireRequestResource(plantHireRequest));
		}
		return new PlantHireRequestResourceList(plantHireRequestResources);
	}
	
	@Override
	public PlantHireRequestResource toResource(PlantHireRequest plantHireRequest) {
		return getPlantHireRequestResource(plantHireRequest); 
	}
}
