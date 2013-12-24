package biz.buildit.service;

import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.w3c.dom.Document;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.rest.InvoiceResource;
import biz.buildit.util.InvoiceDisassembler;

public class InvoiceHumanAssistedHandling {

	public MailMessage process(Document invoiceFile) throws JAXBException{

		MailMessage mailMessage = new SimpleMailMessage(); 

		PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();
		JAXBContext jaxbCtx = JAXBContext.newInstance(InvoiceResource.class); 
		InvoiceResource invoiceResource = (InvoiceResource) jaxbCtx 
				.createUnmarshaller().unmarshal(invoiceFile);

		InvoiceDisassembler.makeInvoice(invoiceResource);
		mailMessage.setTo(propertiesHolder.getRentItEmailAddress()); 
		mailMessage.setSentDate(new Date(System.currentTimeMillis())); 
		mailMessage.setSubject("Payment in process."); 
		mailMessage.setText("The payment is being processed. "
				+ "It would be done as soon as the SiteEngineer approves your Invoice."); 

		return mailMessage;
	}

}
