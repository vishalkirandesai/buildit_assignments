package biz.buildit.web.we;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.exception.InvalidDatesException;
import biz.buildit.exception.POSTRequestException;
import biz.buildit.main.PlantHireRequest;
import biz.buildit.main.PurchaseOrder;
import biz.buildit.rest.PurchaseOrderResource;
import biz.buildit.util.Approval;
import biz.buildit.util.POStatus;
import biz.buildit.util.PurchaseOrderResourceAssembler;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@RequestMapping("/we/phrs")
@Controller
@RooWebScaffold(path = "we/phrs", formBackingObject = PlantHireRequest.class, create = false)
public class PHRWEController {
	
	Client client = new Client();
	ClientResponse clientResponse;
	WebResource webResource;
	PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();
	
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PlantHireRequest plantHireRequest, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws POSTRequestException, InvalidDatesException {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, plantHireRequest);
            return "we/phrs/update";
        }
        PlantHireRequest plantHireRequestOriginal = 
        		PlantHireRequest.findPlantHireRequest(plantHireRequest.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(plantHireRequestOriginal.getWEng().getUsername().equals(authentication.getName())){
        	
        	if(plantHireRequestOriginal.getApproval().equals(Approval.PENDINGAPPROVAL)||
        			plantHireRequestOriginal.getApproval().equals(Approval.REJECTED)){
            	uiModel.asMap().clear();
        		if(plantHireRequest.getApproval().equals(Approval.APPROVED)){
        			if(generatePurchaseOrder(plantHireRequestOriginal)){
        				plantHireRequestOriginal.setApproval(Approval.APPROVED);
                    	plantHireRequestOriginal.merge();
        			}
        			else
        				throw new POSTRequestException("Unable to generate a PO at this moment. Please check the URL if correct. Requested URL :"
        						+propertiesHolder.getRentItBaseURL()+propertiesHolder.getRentItRest()+propertiesHolder.getRentItRestPO());
        		}
        		else{
        			plantHireRequestOriginal.setStartDate(plantHireRequest.getStartDate());
        			plantHireRequestOriginal.setEndDate(plantHireRequest.getEndDate());
        			plantHireRequestOriginal.setApproval(plantHireRequest.getApproval());
        			plantHireRequestOriginal.merge();
        		}
        	}
        	else if(plantHireRequest.getApproval().equals(Approval.APPROVED)){
        		if(plantHireRequest.getExtensionDate() == null)
        			return "Only extension is allowed for approved PHRs";
        		plantHireRequestOriginal.setExtensionDate(plantHireRequest.getExtensionDate());
        		extendPurchaseOrder(plantHireRequestOriginal);
        	}
        }
        return "redirect:/we/phrs/" + encodeUrlPathSegment(plantHireRequest.getId().toString(), httpServletRequest);
    }
	
	private boolean generatePurchaseOrder(PlantHireRequest plantHireRequest){
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPlant(plantHireRequest.getPlant());
		purchaseOrder.setStartDate(plantHireRequest.getStartDate());
		purchaseOrder.setEndDate(plantHireRequest.getEndDate());
		purchaseOrder.setSiteId(plantHireRequest.getSiteId());
		purchaseOrder.setPrice(plantHireRequest.getPrice());
		purchaseOrder.setPoStatus(POStatus.PENDINGAPPROVAL);
		purchaseOrder.persist();
		PurchaseOrderResource purchaseOrderResource = 
				PurchaseOrderResourceAssembler.getResource(purchaseOrder);
		client.addFilter(new HTTPBasicAuthFilter(propertiesHolder.getRentItLoginUserName(), propertiesHolder.getRentItLoginPassword()));
		webResource = client.resource(propertiesHolder.getRentItBaseURL()+
										propertiesHolder.getRentItRest()+
											propertiesHolder.getRentItRestPO());
		
		clientResponse = webResource.type(MediaType.APPLICATION_XML)
					.accept(MediaType.APPLICATION_XML)
						.post(ClientResponse.class,
							purchaseOrderResource);
		if(clientResponse.getStatus() != HttpStatus.CREATED.value()){
			purchaseOrder.remove();
			return false;
		}
		Long poId = clientResponse.getEntity(PurchaseOrderResource.class).getPoId();
		purchaseOrder.setPoId(poId);
		purchaseOrder.merge();
		plantHireRequest.setPurchaseOrder(purchaseOrder);
		plantHireRequest.setPurchaseOrderURI(clientResponse.getLocation());
		return true;
		
	}
	
	private void extendPurchaseOrder(PlantHireRequest plantHireRequest) throws InvalidDatesException{
		webResource = client.resource(plantHireRequest.getPurchaseOrderURI());
		clientResponse = webResource.get(ClientResponse.class);
		PurchaseOrderResource purchaseOrderResource = clientResponse.getEntity(PurchaseOrderResource.class);
		if(plantHireRequest.getExtensionDate().getTime() > System.currentTimeMillis() &&
				plantHireRequest.getExtensionDate().getTime() > plantHireRequest.getEndDate().getTime())
			purchaseOrderResource.setEndDate(plantHireRequest.getExtensionDate().getTime());
		else
			throw new InvalidDatesException("Incorrect extension date");
		webResource.type(MediaType.APPLICATION_XML)
					.put(PurchaseOrderResource.class);
	}
}
