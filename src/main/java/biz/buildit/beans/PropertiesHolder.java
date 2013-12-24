package biz.buildit.beans;

public class PropertiesHolder {
	
	private static String rentItId;

	private static String rentItBaseURL;
	
	private static String rentItRest;
	
	private static String rentItLoginUserName;
	
	private static String rentItLoginPassword;
	
	private static String rentItRestPlant;
	
	private static String rentItRestPO;
	
	private static String rentItEmailAddress;
	
	private static String applicationName;
	
	private static String applicationURL;
	
	private static String companyName;
	
	private static String emailUserName;
	
	private static String emailPassword;
	
	private static String emailClient;

	private static PropertiesHolder instance;
	
	public static PropertiesHolder getInstance(){
		if(instance == null){
			instance = new PropertiesHolder();
			return instance;
		}
		return instance;
	}
	
	public String getRentItBaseURL() {
		return rentItBaseURL;
	}

	public void setRentItBaseURL(String rentItBaseURL) {
		PropertiesHolder.rentItBaseURL = rentItBaseURL;
	}

	public String getRentItEmailAddress() {
		return rentItEmailAddress;
	}

	public void setRentItEmailAddress(String rentItEmailAddress) {
		PropertiesHolder.rentItEmailAddress = rentItEmailAddress;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		PropertiesHolder.applicationName = applicationName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		PropertiesHolder.companyName = companyName;
	}

	public String getEmailUserName() {
		return emailUserName;
	}

	public void setEmailUserName(String emailUserName) {
		PropertiesHolder.emailUserName = emailUserName;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		PropertiesHolder.emailPassword = emailPassword;
	}

	public String getEmailClient() {
		return emailClient;
	}

	public void setEmailClient(String emailClient) {
		PropertiesHolder.emailClient = emailClient;
	}

	public String getApplicationURL() {
		return applicationURL;
	}

	public void setApplicationURL(String applicationURL) {
		PropertiesHolder.applicationURL = applicationURL;
	}

	public String getRentItRest() {
		return rentItRest;
	}

	public void setRentItRest(String rentItRest) {
		PropertiesHolder.rentItRest = rentItRest;
	}

	public String getRentItRestPO() {
		return rentItRestPO;
	}

	public void setRentItRestPO(String rentItRestPO) {
		PropertiesHolder.rentItRestPO = rentItRestPO;
	}

	public String getRentItRestPlant() {
		return rentItRestPlant;
	}

	public void setRentItRestPlant(String rentItRestPlant) {
		PropertiesHolder.rentItRestPlant = rentItRestPlant;
	}

	public String getRentItId() {
		return rentItId;
	}

	public void setRentItId(String rentItId) {
		PropertiesHolder.rentItId = rentItId;
	}

	public String getRentItLoginUserName() {
		return rentItLoginUserName;
	}

	public void setRentItLoginUserName(String rentItLoginUserName) {
		PropertiesHolder.rentItLoginUserName = rentItLoginUserName;
	}

	public String getRentItLoginPassword() {
		return rentItLoginPassword;
	}

	public void setRentItLoginPassword(String rentItPassword) {
		PropertiesHolder.rentItLoginPassword = rentItPassword;
	}
}
