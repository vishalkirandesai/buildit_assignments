package biz.buildit.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

import biz.buildit.util.ResourceSupport;

@RooJavaBean
@XmlRootElement(name="purchaseorder")
public class PurchaseOrderResource extends ResourceSupport{

	private Long poId;
    private int siteId;

    private String companyName;

    private Long startDate;
    private Long endDate;

    private float price;

    private Long plantId;
    
    private String email;
    
}
