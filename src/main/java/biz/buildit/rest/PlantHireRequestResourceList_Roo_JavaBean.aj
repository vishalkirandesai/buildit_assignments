// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package biz.buildit.rest;

import biz.buildit.rest.PlantHireRequestResource;
import biz.buildit.rest.PlantHireRequestResourceList;
import java.util.List;

privileged aspect PlantHireRequestResourceList_Roo_JavaBean {
    
    public List<PlantHireRequestResource> PlantHireRequestResourceList.getPlantHireRequestResources() {
        return this.plantHireRequestResources;
    }
    
    public void PlantHireRequestResourceList.setPlantHireRequestResources(List<PlantHireRequestResource> plantHireRequestResources) {
        this.plantHireRequestResources = plantHireRequestResources;
    }
    
}
