package biz.buildit.util;

import biz.buildit.main.SiteEngineer;
import biz.buildit.rest.SiteEngineerResource;


public class SiteEngineerResourceAssembler{
	
	public static SiteEngineerResource getResource(SiteEngineer siteEngineer){
		SiteEngineerResource siteEngineerResource = new SiteEngineerResource();
		siteEngineerResource.setId(siteEngineer.getId());
		siteEngineerResource.setName(siteEngineer.getUsername());
		return siteEngineerResource;
	}
}
