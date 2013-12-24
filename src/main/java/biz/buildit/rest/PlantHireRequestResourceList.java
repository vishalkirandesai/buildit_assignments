package biz.buildit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@XmlRootElement(name="planthirerequests")
public class PlantHireRequestResourceList {
	
	List<PlantHireRequestResource> plantHireRequestResources;
	
	public PlantHireRequestResourceList(){
		plantHireRequestResources = new ArrayList<PlantHireRequestResource>();
	}
	
	public PlantHireRequestResourceList(List<PlantHireRequestResource> plantHireRequestResources){
		this.plantHireRequestResources = plantHireRequestResources;
	}
	
}
