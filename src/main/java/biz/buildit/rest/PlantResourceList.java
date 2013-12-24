package biz.buildit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@XmlRootElement(name="plants")
public class PlantResourceList {

	List<PlantResource> plantResources;
	
	public PlantResourceList(){
		plantResources = new ArrayList<PlantResource>();
	}
	
	public PlantResourceList(List<PlantResource> plantResources){
		this.plantResources = plantResources;
	}
}
