package biz.buildit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@XmlRootElement(name="purchaseorders")
public class PurchaseOrderResourceList {

	List<PurchaseOrderResource> purchaseOrderResources;
	
	public PurchaseOrderResourceList(){
		purchaseOrderResources = new ArrayList<PurchaseOrderResource>();
	}
}
