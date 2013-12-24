package biz.buildit.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import biz.buildit.main.PurchaseOrder;
import biz.buildit.rest.PurchaseOrderResource;
import biz.buildit.util.Approval;
import biz.buildit.util.ExtendedLink;
import biz.buildit.util.POStatus;
import biz.buildit.util.PurchaseOrderResourceAssembler;

@Controller
@RequestMapping(value="/rest/purchaseorders")
public class PurchaseOrderRestController {

	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public ResponseEntity<PurchaseOrderResource> getPurchaseOrder(@PathVariable Long id) throws NoSuchMethodException, SecurityException{
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(id);
		PurchaseOrderResource purchaseOrderResource = PurchaseOrderResourceAssembler.getResource(purchaseOrder);
		switch(purchaseOrder.getPoStatus()){
		case PENDINGAPPROVAL:
			Method _rejectPO = PurchaseOrderRestController.class.getMethod("rejectPO",Long.class); 
			Method _approvePO = PurchaseOrderRestController.class.getMethod("approvePO",Long.class); 
			 
			String approveLink = linkTo(_approvePO, purchaseOrder.getId()).toUri().toString(); 
			purchaseOrderResource.add(new ExtendedLink(approveLink, "approvePO", "POST")); 

			String rejectLink = linkTo(_rejectPO, purchaseOrder.getId()).toUri().toString(); 
			purchaseOrderResource.add(new ExtendedLink(rejectLink, "rejectPO", "DELETE")); 
			break;
		default: break;
		}
		ResponseEntity<PurchaseOrderResource> responseEntity = 
				new ResponseEntity<PurchaseOrderResource>
		(purchaseOrderResource,HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/{id}/approval")
	public ResponseEntity<Void> acceptPO(@PathVariable Long id){
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(id);
		ResponseEntity<Void> responseEntity;
		if(purchaseOrder.getPoStatus() == POStatus.PENDINGAPPROVAL){
			purchaseOrder.setPoStatus(POStatus.ACCEPTED);
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		}
		else
		{
			responseEntity = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return responseEntity;
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}/approval")
	public ResponseEntity<Void> rejectPO(@PathVariable Long id){
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(id);
		ResponseEntity<Void> responseEntity;
		if(purchaseOrder.getPoStatus() == POStatus.PENDINGAPPROVAL){
			purchaseOrder.setPoStatus(POStatus.REJECTED);
			responseEntity = new ResponseEntity<>(HttpStatus.OK);
		}
		else
		{
			responseEntity = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
		return responseEntity;
	}
}
