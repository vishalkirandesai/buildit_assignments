package biz.buildit.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.roo.addon.javabean.RooJavaBean;

import biz.buildit.util.ResourceSupport;

@RooJavaBean
@XmlRootElement(name="invoice")
public class InvoiceResource extends ResourceSupport{
	
	private Long dueDate;
	
	private float total;
	
	private Long poId;
}
