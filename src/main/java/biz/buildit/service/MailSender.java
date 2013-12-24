package biz.buildit.service;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import biz.buildit.beans.PropertiesHolder;
import biz.buildit.util.MailClient;

public class MailSender extends JavaMailSenderImpl{

	private static MailSender instance;
	private Properties prop;
	private PropertiesHolder propertiesHolder;
	
	public MailSender(MailClient client){
		propertiesHolder = PropertiesHolder.getInstance();
		switch(client){
		case GMAIL:
			setUsername(propertiesHolder.getEmailUserName());
			setPassword(propertiesHolder.getEmailPassword());
			setHost("smtp.gmail.com");
			setPort(587);
			prop = new Properties();
			prop.setProperty("mail.smtp.starttls.enable", Boolean.toString(true));
			prop.setProperty("mail.smtp.auth", Boolean.toString(true));
			setJavaMailProperties(prop);
			break;
		default: 
			setUsername(propertiesHolder.getEmailUserName());
			setPassword(propertiesHolder.getEmailPassword());
			setHost("smtp.gmail.com");
			setPort(587);
			prop = new Properties();
			prop.setProperty("mail.smtp.starttls.enable", Boolean.toString(true));
			prop.setProperty("mail.smtp.auth", Boolean.toString(true));
			setJavaMailProperties(prop);
			break;
		}
	}
	public static MailSender getInstance(){
		if(instance == null)
			instance = new MailSender(MailClient.GMAIL);
		return instance;
	}
	
	public static MailSender getInstance(MailClient client){
		if(instance == null)
			instance = new MailSender(client);
		return instance;
	}
	
	public void setProperty(String key,String value){
		prop.setProperty(key, value);
	}

}
