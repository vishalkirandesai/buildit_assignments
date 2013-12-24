package biz.buildit.web;
import java.util.Arrays;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.main.Plant;
import biz.buildit.rest.PlantResourceList;
import biz.buildit.util.ResourceDisassembler;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.Base64;

@RequestMapping("/plantcatalogues")
@Controller
@RooWebScaffold(path = "plantcatalogues", formBackingObject = Plant.class)
public class PlantCatalogueController {
	PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();
	Client client = new Client();
	WebResource webResource;
	ClientResponse clientResponse;

	static HttpHeaders getHeaders(String auth) { 
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON); 
		headers.setAccept(Arrays. 
				asList(org.springframework.http.MediaType.APPLICATION_JSON)); 

		byte[] encodedAuthorisation = Base64.encode(auth.getBytes()); 
		headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
		return headers; 
	} 

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
 
		String url = propertiesHolder.getRentItBaseURL()+
				propertiesHolder.getRentItRest()+
				propertiesHolder.getRentItRestPlant();
		client.addFilter(new HTTPBasicAuthFilter(propertiesHolder.getRentItLoginUserName(),propertiesHolder.getRentItLoginPassword()));
		webResource = client.resource(url);
		clientResponse = webResource.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);
		PlantResourceList plantResourceList = webResource.get(PlantResourceList.class);
		ResourceDisassembler.refreshPlantList(plantResourceList);
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("plants", Plant.findPlantEntries(firstResult, sizeNo));
			float nrOfPages = (float) Plant.countPlants() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("plants", Plant.findAllPlants());
		}
		addDateTimeFormatPatterns(uiModel);
		return "plantcatalogues/list";
	}

	@RequestMapping(value="/available/{startDate}/endDate/")
	public String listAvailablePlants(@PathVariable Date startDate,@PathVariable Date endDate){
		HttpEntity<String> requestEntity = new HttpEntity<String>( getHeaders("admin" + ":" + "admin")); 
		String url = propertiesHolder.getRentItBaseURL()+
				propertiesHolder.getRentItRest()+
				propertiesHolder.getRentItRestPlant()+"/"+startDate+"/"+endDate;
		RestTemplate template = new RestTemplate(); 
		ResponseEntity<PlantResourceList> response = template.exchange(url,
				HttpMethod.GET, requestEntity, PlantResourceList.class); 

		PlantResourceList plantResourceList = response.getBody();
		ResourceDisassembler.refreshPlantList(plantResourceList);
		return "plantcatalogues/list";
	}
}
