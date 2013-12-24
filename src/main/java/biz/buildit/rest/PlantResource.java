package biz.buildit.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@XmlRootElement(name="plant")
public class PlantResource {
	
	private Long pId;
	private float price;
	private double CostPerDay;//group4
	private String name;
	private Long startDate;
	private Long endDate;
	private String description;
}
