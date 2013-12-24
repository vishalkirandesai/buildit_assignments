package biz.buildit.util;

import biz.buildit.main.PurchaseOrder;
import biz.buildit.rest.PurchaseOrderResource;

public class PurchaseOrderResourceAssembler {
	
	public static PurchaseOrderResource getResource(PurchaseOrder purchaseOrder){
		PurchaseOrderResource purchaseOrderResource = new PurchaseOrderResource();
		purchaseOrderResource.setPoId(purchaseOrder.getId());
		purchaseOrderResource.setCompanyName("BuildIt Corp.Ltd");
		purchaseOrderResource.setSiteId(purchaseOrder.getSiteId());
		purchaseOrderResource.setStartDate(purchaseOrder.getStartDate().getTime());
		purchaseOrderResource.setEndDate(purchaseOrder.getEndDate().getTime());
		purchaseOrderResource.setPrice(purchaseOrder.getPrice());
		purchaseOrderResource.setPlantId(purchaseOrder.getPlant().getPId());
		purchaseOrderResource.setEmail("builtit.finally@gmail.com");
		return purchaseOrderResource;
	}
}
