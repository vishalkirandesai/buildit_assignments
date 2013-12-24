package biz.buildit.util;

import java.util.Date;

import biz.buildit.main.Invoice;
import biz.buildit.main.PurchaseOrder;
import biz.buildit.rest.InvoiceResource;

public class InvoiceDisassembler {

	public static Invoice makeInvoice(InvoiceResource invoiceResource){
		Invoice invoice = new Invoice();
		invoice.setInvoiceStatus(InvoiceStatus.PENDINGAPPROVAL);
		invoice.setDueDate(new Date(invoiceResource.getDueDate()));
		invoice.setTotal(invoiceResource.getTotal());
		invoice.setPoId(invoiceResource.getPoId());
		invoice.persist();
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(invoiceResource.getPoId());
		purchaseOrder.setInvoice(invoice);
		purchaseOrder.merge();
		return invoice;
	}
}
