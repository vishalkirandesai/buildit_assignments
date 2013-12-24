package biz.buildit.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import biz.buildit.main.Plant;
import biz.buildit.rest.PlantResource;

@Controller
@RequestMapping(value="/rest/plants")
public class PlantRestController {
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<PlantResource> createPlant(@RequestBody PlantResource plantResource){
		ResponseEntity<PlantResource> responseEntity;
		if(plantResource.getPrice() == 0.0 ||
				plantResource.getName() == null ){
			responseEntity = new ResponseEntity<PlantResource>(plantResource,HttpStatus.NOT_ACCEPTABLE);
			return responseEntity;
		}
		Plant plant = new Plant();
		plant.setName(plantResource.getName());
		plant.setDescription(plantResource.getDescription());
		plant.setPrice(plantResource.getPrice());
		plant.persist();
		plantResource.setPId(plant.getPId());
		responseEntity = new ResponseEntity<>(plantResource,HttpStatus.CREATED);
		return responseEntity;
	}

}
