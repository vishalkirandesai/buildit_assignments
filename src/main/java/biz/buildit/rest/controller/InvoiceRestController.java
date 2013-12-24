package biz.buildit.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.lang.reflect.Method;
import java.util.Date;

import javax.mail.Header;
import javax.ws.rs.core.Request;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.main.Invoice;
import biz.buildit.main.PlantHireRequest;
import biz.buildit.rest.InvoiceResource;
import biz.buildit.service.MailSender;
import biz.buildit.util.Approval;
import biz.buildit.util.ExtendedLink;
import biz.buildit.util.InvoiceResourceAssembler;
import biz.buildit.util.InvoiceStatus;
import biz.buildit.util.MailClient;

@Controller
@RequestMapping(value="/rest/invoices")
public class InvoiceRestController {
	
	PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();

	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public ResponseEntity<InvoiceResource> getInvoices(@PathVariable Long id) throws NoSuchMethodException, SecurityException{
		Invoice invoice = Invoice.findInvoice(id);
		InvoiceResource invoiceResource = InvoiceResourceAssembler.getResource(invoice);
		switch (invoice.getInvoiceStatus()) { 
		case PENDINGAPPROVAL: 
			Method _rejectInvoice=InvoiceRestController.class.getMethod("rejectInvoice",Long.class); 
			Method _approveInvoice=InvoiceRestController.class.getMethod("approveInvoice",Long.class); 

			String approveLink = linkTo(_approveInvoice, invoice.getId()).toUri().toString(); 
			invoiceResource.add(new ExtendedLink(approveLink, "approveInvoice", "PUT")); 

			String rejectLink = linkTo(_rejectInvoice, invoice.getId()).toUri().toString(); 
			invoiceResource.add(new ExtendedLink(rejectLink, "rejectInvoice", "PUT")); 
			break; 

		default: 
			break; 
		} 

		ResponseEntity<InvoiceResource> responseEntity = 
				new ResponseEntity<>(invoiceResource,HttpStatus.OK);

		return responseEntity;
	}

	@RequestMapping(method=RequestMethod.PUT, value="/{id}/approval")
	public ResponseEntity<Void> approveInvoice(@PathVariable Long id){
		Invoice invoice = Invoice.findInvoice(id); 
		ResponseEntity<Void> response; 

		if (invoice.getInvoiceStatus().equals(Approval.PENDINGAPPROVAL)) { 
			invoice.setInvoiceStatus(InvoiceStatus.ACCEPTED);
			MailSender mailSender = MailSender.getInstance(MailClient.GMAIL);
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(propertiesHolder.getRentItEmailAddress());
			simpleMailMessage.setSubject("Processed Invoice");
			simpleMailMessage.setText("The Site Engineer has approved the invoice and "
					+ "your payment is on the way. Great doing business with you.");
			simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
			mailSender.send(simpleMailMessage);
			response = new ResponseEntity<>(HttpStatus.OK); 
		} else 
			response = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED); 

		return response; 
	}

	@RequestMapping(method=RequestMethod.PUT, value="/{id}/rejection")
	public ResponseEntity<Void> rejectInvoice(@PathVariable Long id){
		Invoice invoice = Invoice.findInvoice(id); 
		ResponseEntity<Void> response; 

		if (invoice.getInvoiceStatus().equals(Approval.PENDINGAPPROVAL)) { 
			invoice.setInvoiceStatus(InvoiceStatus.REJECTED);
			MailSender mailSender = MailSender.getInstance(MailClient.GMAIL);
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(propertiesHolder.getRentItEmailAddress());
			simpleMailMessage.setSubject("Processed Invoice");
			simpleMailMessage.setText("We are very sorry but your invoice is ridiculous."
					+ "Send a better one next time.");
			simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
			mailSender.send(simpleMailMessage);
			response = new ResponseEntity<>(HttpStatus.OK); 
		} else 
			response = new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED); 

		return response; 
	}

}
