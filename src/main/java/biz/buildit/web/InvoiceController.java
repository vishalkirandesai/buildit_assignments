package biz.buildit.web;
import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.main.Invoice;
import biz.buildit.main.PurchaseOrder;
import biz.buildit.service.MailSender;
import biz.buildit.util.InvoiceStatus;
import biz.buildit.util.MailClient;

@RequestMapping("/invoices")
@Controller
@RooWebScaffold(path = "invoices", formBackingObject = Invoice.class)
public class InvoiceController {
	
	static PropertiesHolder propertiesHolder = PropertiesHolder.getInstance();

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("invoice", Invoice.findInvoice(id));
        uiModel.addAttribute("itemId", id);
        return "invoices/show";
    }
	
	@RequestMapping(value = "/{id}/approve", produces = "text/html")
    public String approveInvoice(@PathVariable("id") Long id, Model uiModel) {
		Invoice invoice = Invoice.findInvoice(id);
		invoice.setInvoiceStatus(InvoiceStatus.ACCEPTED);
		invoice.persist();
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("invoice", Invoice.findInvoice(id));
        uiModel.addAttribute("itemId", id);
        return "redirect:/invoices/"+id;
    }
	
	@RequestMapping(value = "/{id}/reject", produces = "text/html")
    public String rejectInvoice(@PathVariable("id") Long id, Model uiModel) {
		Invoice invoice = Invoice.findInvoice(id);
		invoice.setInvoiceStatus(InvoiceStatus.REJECTED);
		invoice.merge();
		PurchaseOrder purchaseOrder = PurchaseOrder.findPurchaseOrder(invoice.getPoId());
		purchaseOrder.setInvoice(null);
		purchaseOrder.merge();
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("invoice", Invoice.findInvoice(id));
        uiModel.addAttribute("itemId", id);
        return "redirect:/invoices/"+id;
    }
	
	@RequestMapping(value = "/{id}/pay", produces = "text/html")
    public String payInvoice(@PathVariable("id") Long id, Model uiModel) {
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice.getInvoiceStatus() == InvoiceStatus.ACCEPTED){
			makePayment(invoice);
			invoice.setInvoiceStatus(InvoiceStatus.PAID);
			invoice.merge();
		}
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("invoice", Invoice.findInvoice(id));
        uiModel.addAttribute("itemId", id);
        return "redirect:/invoices/"+id;
    }
	
	static void makePayment(Invoice invoice){
		// payment logic
		MailSender mailSender = MailSender.getInstance(MailClient.GMAIL);
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(propertiesHolder.getRentItEmailAddress());
		simpleMailMessage.setSubject("Processed Invoice");
		simpleMailMessage.setText("The Site Engineer has approved the invoice and "
				+ "your payment is on the way. Great doing business with you.");
		simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
		mailSender.send(simpleMailMessage);
	}
}
