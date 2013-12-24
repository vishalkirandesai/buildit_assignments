package biz.buildit.service;

import java.util.Date;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.mail.SimpleMailMessage;
import org.w3c.dom.Document;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.main.Invoice;
import biz.buildit.main.PurchaseOrder;
import biz.buildit.util.InvoiceStatus;
import biz.buildit.util.MailClient;

public class InvoiceAutomaticProcessor {

	public void process(Document invoiceMail) throws NumberFormatException, XPathExpressionException{
		XPath xPath = XPathFactory.newInstance().newXPath();
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(Long.parseLong(xPath.evaluate("//poId",invoiceMail)));
		if(purchaseOrder.getInvoice() != null){
			Invoice invoice = purchaseOrder.getInvoice();
			if(invoice.getInvoiceStatus() == InvoiceStatus.DUEPAYMENT )
				if(makePayment(invoice)){
						invoice.setInvoiceStatus(InvoiceStatus.PAID);
						invoice.merge();
				}
		}
		else {
			Invoice invoice = new Invoice();
			invoice.setTotal(Float.parseFloat(xPath.evaluate("//total",invoiceMail)));
			invoice.setDueDate(new Date(Long.parseLong(xPath.evaluate("//dueDate",invoiceMail))));
			invoice.setInvoiceStatus(InvoiceStatus.DUEPAYMENT);
			invoice.setPoId(Long.parseLong(xPath.evaluate("//poId",invoiceMail)));
			if(makePayment(invoice))
				invoice.setInvoiceStatus(InvoiceStatus.PAID);
			invoice.persist();
			purchaseOrder.setInvoice(invoice);
			purchaseOrder.merge();
			PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();
			MailSender mailSender = MailSender.getInstance(MailClient.GMAIL);
			SimpleMailMessage simpleMailMessage =
					new SimpleMailMessage();
			simpleMailMessage.setSubject("Payment has being processed");
			simpleMailMessage.setTo(propertiesHolder.getRentItEmailAddress());
			simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
			simpleMailMessage.setText("Your payment has been made.");
			mailSender.send(simpleMailMessage);
		}
		
	}
	
	private boolean makePayment(Invoice invoice){
		if(invoice.getInvoiceStatus() == InvoiceStatus.DUEPAYMENT)
			return true;
		else
			return false;
	}
}
