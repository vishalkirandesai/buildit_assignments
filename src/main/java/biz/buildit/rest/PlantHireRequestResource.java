package biz.buildit.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

import biz.buildit.util.Approval;
import biz.buildit.util.ResourceSupport;

@RooJavaBean
@XmlRootElement(name="planthirerequest")
public class PlantHireRequestResource extends ResourceSupport{

    private int siteId;

    private SiteEngineerResource siteEngineerResource;

    private Long startDate;

    private Long endDate;

    private Long extensionDate;

    private String comments;

    private PlantResource plantResource;

    private WorksEngineerResource wEngineerResource;
    
    private int approval;
    
}
