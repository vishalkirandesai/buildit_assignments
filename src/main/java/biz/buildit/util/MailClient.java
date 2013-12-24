package biz.buildit.util;


public enum MailClient {

	GMAIL,
	YAHOO,
	HOTMAIL,
	UT;
	
	public static MailClient getClient(String client){
		switch(client.toLowerCase()){
		case "gmail":
			return MailClient.GMAIL;
		case "yahoo":
			return MailClient.YAHOO;
		case "hotmail":
			return MailClient.HOTMAIL;
		case "ut":
			return MailClient.UT;
		default: 
			return MailClient.GMAIL;
		}
	}
}
