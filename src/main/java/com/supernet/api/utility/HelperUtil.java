package com.supernet.api.utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class HelperUtil {

	public static String createOutputDir(String folderPath) {
		try {
			String path = new File(".").getCanonicalPath() + File.separator + "testdata";
			path += File.separator + folderPath;
			File pathObj = new File(path);
			if (!pathObj.exists()) {
				pathObj.mkdirs();
			}
			return path;
		} catch (IOException e) {
			// logger.error("Failed to create Reports directory : ", e);
			return null;
		}
	}

	public static String getUniqueValue() {
		DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String uniqueValue = (String) dateFormat.format(date);
		return uniqueValue;
	}
	
	public static boolean sendMailwithAttachment(String gmailID,
			String password, String toEmailID, String toName, String attachmentPath) {

		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();

		// attachment.setPath(new File(".").getCanonicalPath()
		// + File.separator + "test-output" + File.separator
		// + "emailable-report.html");
		attachment.setPath(attachmentPath);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Status of Automation");
		attachment.setName("Report.html");

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(25);
		email.setAuthenticator(new DefaultAuthenticator(gmailID, password));
		email.setSSLOnConnect(true);
		try {
			email.addTo(toEmailID, toName);
			email.setFrom(gmailID, "Test Execution report");
			email.setSubject("Automation Test Execution Report");
			email.setMsg("Here is the automation test execution report.");
			email.attach(attachment);
			email.send();
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static String getUniqueEmail(String email, String uniqueValue) {
		
		String[] mailaddress = email.split("@");
		
		String mail = mailaddress[0]+uniqueValue;
		email = mail+mailaddress[1];
		return email;
	}
	
	public void uploadCSV(String authorization){
		
	}
	
	public void createStructureWorkArea(String authorization, String datasourceId){
		
	}

}

