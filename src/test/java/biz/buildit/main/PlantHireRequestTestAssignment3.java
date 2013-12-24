package biz.buildit.main;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.rest.PlantHireRequestResource;
import biz.buildit.rest.PlantResource;
import biz.buildit.rest.PlantResourceList;
import biz.buildit.rest.PurchaseOrderResource;
import biz.buildit.rest.SiteEngineerResource;
import biz.buildit.rest.WorksEngineerResource;
import biz.buildit.util.Approval;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class PlantHireRequestTestAssignment3 {

	PlantHireRequestResource plantHireRequestResource;
	PlantResource plantResource;
	Client clientSE;
	Client clientWE;
	Client clientCustomer;
	WebResource webResource;
	ClientResponse clientResponse;

	public PlantHireRequestTestAssignment3(){
		clientSE = new Client();
		clientSE.addFilter(new HTTPBasicAuthFilter("vishal","vishal123"));
		clientWE = new Client();
		clientWE.addFilter(new HTTPBasicAuthFilter("jake","jake123"));
		clientCustomer = new Client();
		clientCustomer.addFilter(new HTTPBasicAuthFilter("customer","customer"));
		webResource = clientCustomer.resource("http://rentit13.herokuapp.com/rest/plants");
		clientResponse = webResource.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);
		//random first plant from the list of plants on RentIt
		PlantResourceList plantResourceList = clientResponse.getEntity(PlantResourceList.class);
		PlantResource plantResource = plantResourceList.getPlantResources().get(0);

		//ROLE_SITE_ENGINEER
		SiteEngineerResource siteEngineerResource = new SiteEngineerResource();
		siteEngineerResource.setName("vishal");
		//ROLE_WORKS_ENGINEER
		WorksEngineerResource worksEngineerResource = new WorksEngineerResource();
		worksEngineerResource.setName("jake");

		plantHireRequestResource = new PlantHireRequestResource();
		plantHireRequestResource.setPlantResource(plantResource);
		plantHireRequestResource.setStartDate(1389953223000L);
		plantHireRequestResource.setEndDate(1390558023000L);
		plantHireRequestResource.setSiteId(15);
		plantHireRequestResource.setSiteEngineerResource(siteEngineerResource);
		plantHireRequestResource.setWEngineerResource(worksEngineerResource);




	}
	@Test
	public void theReallyBigTestWhereWeTestEverythingAtOnce(){
		URI uri;
		// firstly create the phr that we defined in the constructor of this test class.
		webResource = clientSE.resource("http://localhost:8080/BuildIt/rest/planthirerequests");
		clientResponse = webResource.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class,plantHireRequestResource);
		// test for successful creation
		Assert.assertTrue(HttpStatus.CREATED.value() == clientResponse.getResponseStatus().getStatusCode());

		uri = clientResponse.getLocation();

		webResource = clientSE.resource(uri);

		clientResponse = webResource.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);

		PlantHireRequestResource plantHireRequestResource = clientResponse.getEntity(PlantHireRequestResource.class);

		// check for successful link retrieval using proper the proper rel
		Assert.assertNotNull(plantHireRequestResource.get_link("approvePHR").getHref());

		// approve the phr using the link. Observe that this time I made use of the other client
		// here the credentials are that of a WE.
		webResource = clientWE.resource(plantHireRequestResource.get_link("approvePHR").getHref());
		clientResponse = webResource.type(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)
				.put(ClientResponse.class);

		// if response is ok then we proceed with two more test else the test case fails.
		if(clientResponse.getResponseStatus().getStatusCode() == HttpStatus.OK.value()){
			PurchaseOrderResource purchaseOrderResource = clientResponse.getEntity(PurchaseOrderResource.class);
			// check that POResource is not null
			Assert.assertNotNull(purchaseOrderResource);

			webResource = clientCustomer.resource(purchaseOrderResource.get_link("cancelPurchaseOrder").getHref());

			clientResponse = webResource.type(MediaType.APPLICATION_XML)
					.accept(MediaType.APPLICATION_XML)
					.delete(ClientResponse.class);
			// check that we have managed to invoke the link 
			Assert.assertTrue(HttpStatus.OK.value() == clientResponse.getResponseStatus().getStatusCode());

		}

		else
			Assert.assertTrue(false); // fail the test case since response was not OK
		// make sure that the purchase order is created on rentit else this last assert will fail.
		// this may occur even for ridiculously elementary reasons such as plant not available :D

		Assert.assertEquals(clientResponse.getResponseStatus().getStatusCode(), HttpStatus.OK.value());
		// get the resource and check that we have successfully approved the PHR.
		webResource = clientSE.resource(uri.toString());
		clientResponse = webResource.accept(MediaType.APPLICATION_XML)
				.type(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);

		// check for successful approval.
		Assert.assertEquals(Approval.APPROVED.getStatusCode(),clientResponse.getEntity(PlantHireRequestResource.class).getApproval());



	}
}
