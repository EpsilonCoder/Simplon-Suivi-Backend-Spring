package com.simplonsuivi.co.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

import static com.simplonsuivi.co.constant.EmailConstant.*;

@Service

public class EmailSercice {
	
	 public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
	        Message message = this.createEmail(firstName, password, email);
	        SMTPTransport smtpTransport = (SMTPTransport) this.getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
	        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
	        smtpTransport.sendMessage(message, message.getAllRecipients());
	        smtpTransport.close();
	    }
	
	 private Message createEmail(String firstName, String password, String email) throws MessagingException {
	        Message message = new MimeMessage(this.getEmailSession());
	        message.setFrom(new InternetAddress(FROM_EMAIL));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
	        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
	        message.setSubject(EMAIL_SUBJECT);
	        message.setText("Bonjour ".concat(firstName).concat(",\n\n le mot de pass Simplon-Suivi est :").concat(password).concat("\n\nL'equipe Simplon Senegal."));
	        message.setSentDate(new Date());
	        message.saveChanges();
	        return message;
	    }
	
	private Session getEmailSession() {
		
		Properties properties=System.getProperties();
		properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_PORT, DEFAULT_PORT);
		properties.put(SMTP_STARTTLS_ENABLE, true);
		properties.put(SMTP_STARTTLS_REQUIRED, true);
		
		return Session.getInstance(properties,null);
	}

}
