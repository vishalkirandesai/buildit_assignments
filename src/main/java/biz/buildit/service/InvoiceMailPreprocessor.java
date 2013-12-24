package biz.buildit.service;

import java.io.IOException;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.integration.MessagingException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.main.Invoice;
import biz.buildit.main.PurchaseOrder;
import biz.buildit.util.InvoiceStatus;
import biz.buildit.util.MailClient;
import biz.buildit.util.POStatus;


@Component 
public class InvoiceMailPreprocessor { 

	@ServiceActivator 
	public Document process(Message msg) throws MessagingException, IOException, 
	ParserConfigurationException, SAXException, javax.mail.MessagingException, InstantiationException, IllegalAccessException, NumberFormatException, XPathExpressionException { 
		PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();
		Document invoiceXML = null; 
		Object _content = msg.getContent(); 
		if (_content instanceof Multipart) { 
			Multipart content = (Multipart) _content; 
			for (int i = 0; i < content.getCount(); i++) { 
				BodyPart part = content.getBodyPart(i); 
				if (part.getContentType().startsWith("text/xml") || 
						part.getContentType().startsWith("application/xml")) { 

					String fileName = part.getFileName(); 
					if (fileName.startsWith("invoice")) { 
						DocumentBuilder builder = 
								DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
						invoiceXML = builder.parse(part.getInputStream());
						if(!isInvoiceCorrect(invoiceXML)){
							MailSender mailSender = 
									MailSender.getInstance(MailClient.GMAIL);
							SimpleMailMessage mailMessage =
									new SimpleMailMessage();
							mailMessage.setTo(propertiesHolder.getRentItEmailAddress());
							mailMessage.setText("Please reconfirm invoice statement");
							mailMessage.setSentDate(new Date(System.currentTimeMillis()));
							mailMessage.setSubject("Faulty or Duplicate invoice.");
							mailSender.send(mailMessage);
							break;
						}
						else{
							
						}
						break; 
					} 
				} 
			} 
		} 
		if (invoiceXML == null) 
			throw new IOException("No invoice was found !"); 
		return invoiceXML; 
	} 

	// check for price as well as check for duplicate invoice
	public static boolean isInvoiceCorrect(Document document) throws NumberFormatException, XPathExpressionException{
		XPath xPath = XPathFactory.newInstance().newXPath(); 
		Long poId = Long.parseLong(xPath.evaluate("//poId",document));
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(poId);
		if(purchaseOrder == null)
			return false;
		Invoice invoice = purchaseOrder.getInvoice();
		if(invoice != null)
			return false;
		Float total = purchaseOrder.getPrice();
		Float invoiceTotal = Float.valueOf(xPath.evaluate("//total",document));
		if(total >= invoiceTotal){
			return true;
		}
		return false;
	}
	
} 

