package biz.buildit.util;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import biz.buildit.main.Invoice;
import biz.buildit.rest.InvoiceResource;
import biz.buildit.rest.controller.InvoiceRestController;

public class InvoiceResourceAssembler extends ResourceAssemblerSupport<Invoice,InvoiceResource>{

	public InvoiceResourceAssembler() {
		super(InvoiceRestController.class,InvoiceResource.class);
	}

	@Override
	public InvoiceResource toResource(Invoice invoice) {
		return getResource(invoice);
	}

	public static InvoiceResource getResource(Invoice invoice){
		InvoiceResource invoiceResource = new InvoiceResource();
		invoiceResource.setDueDate(invoice.getDueDate().getTime());
		invoiceResource.setTotal(invoice.getTotal());
		return invoiceResource;
	}
}
