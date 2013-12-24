package biz.buildit.service;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class InvoiceRouter{ 

	// Comparing invoice's total with its corresponding po's total 
	public String analyzeInvoice(Document invoice) throws XPathExpressionException { 
		String destinationChannel = null; 
		XPath xPath = XPathFactory.newInstance().newXPath(); 
		Float total = Float.valueOf(xPath.evaluate("//total", invoice)); 
		if (total <= 100) 
			destinationChannel = "MINOR"; 
		else 
			destinationChannel = "MAJOR"; 

		return destinationChannel; 
	} 
} 
