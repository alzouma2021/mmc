/**
 * 
 */
package com.siliconwise.common.mail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.common.config.data.DataConfigKeys;
import com.siliconwise.common.config.data.Parameter;
import com.siliconwise.common.config.data.ParameterDAO;
import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;

/**
 * Email service
 * @author GNAKALE Bernardin
 *
 */
@Stateful @ApplicationScoped
public class EmailService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String KEY_MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
	private static final String KEY_MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
	private static final String VALUE_MAIL_SMTP_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
	
	public EmailService() throws EmailServiceException {
		initialise();
	}

	private static final String EMAIL_CONFIG_VALUE_TRUE = "true" ;
	private static final String EMAIL_CONFIG_VALUE_FALSE = "false" ;

	// actualisation de balise apres opération de pose et depose
	public static final int POSE = 1 ;
	public static final int DEPOSE = 2 ;
	public static final int RECEPTION_DEPOSE = 3 ;

	private Properties emailServiceConfig = new Properties();
	
	private String mailAuthUser = null ;
	
	private String mailAuthPassword = null;
	
	private boolean isInitialized = false ;
	
	private Properties appConfig = AppConfigUtil.getAppConfig();
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName()) ;
	
	/**
	 * Initialise email service
	 * @throws EmailServiceException
	 */
	private void initialise() throws EmailServiceException
	{		
		String value = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTTLS_ENABLE);
		
		if (value != null && value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
		{
			logger.info("EmailService::initialise: Utilisation de TLS");
			emailServiceConfig = initTLSMailConfig(appConfig);
		}
		else
		{
			value = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTSSL_ENABLE);
			
			if (value != null && value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
			{
				logger.info("EmailService::initialise: Utilisation de SSL");
				emailServiceConfig = initSSLMailConfig(appConfig);
			}
			else
			{
				logger.info("EmailService::initialise: Utilisation d'aucun protocole de securite");
				emailServiceConfig = initNoSecuredProtocolMailConfig(appConfig);
			}
		}
		
		value = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL) ;
		
		try
		{
			InternetAddress ia = new InternetAddress(value);
			ia.validate();
		}
		catch(AddressException aex)
		{
			String msg = "La vaeur du paramètre " + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL
					+ " dans le fichier de configuration n'est pas une adresse email correcte.";
			logger.error(msg + " " + aex.getLocalizedMessage());
			
			throw new EmailServiceException(msg);
		}
				
		// usrname an password must be supplied is auth is set to true

		value = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH);
		
		if (value != null && value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
		{
			mailAuthUser = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_USER);
			mailAuthPassword = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_PASSWORD);
			
			if (mailAuthUser == null || mailAuthUser.equals(""))
			{
				String msg = "Paramètre " + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_USER 
						+ " non défini dans le ficheir de configuration.";
				logger.error(msg);
				throw new EmailServiceException(msg);
			}
			
		}
				
		isInitialized = true;
	}
	
	private Properties initTLSMailConfig(Properties myAppConfig) 
			throws EmailServiceException
	{
		
		Properties props = new Properties();
		String msg = "";
		
		// mail.smtp.auth
		
		String value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH);
		
		if (value != null && !value.equals("") 
				&& (value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE)
						|| value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_FALSE)))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH
					+"' est incorrecte." ;							
		}
		
		// mail.smtp.starttls.enable
		
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTTLS_ENABLE);
		
		if (value != null && !value.equals("") 
				&& (value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE)
						|| value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_FALSE)))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTTLS_ENABLE, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTTLS_ENABLE
					+"' est incorrecte." ;							
		}
	
		// mail.smtp.host
		
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST
					+"' est incorrecte." ;							
		}
		
		// mail.smtp.port
				
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT
					+"' est incorrecte." ;							
		}
		
		if (!msg.equals("")) throw new EmailServiceException(msg);
		
		return(props);		
	}
	
	
	private Properties initSSLMailConfig(Properties myAppConfig) 
			throws EmailServiceException
	{
		Properties props = new Properties();
		String msg = "";
		
		// mail.smtp.auth
		
		String value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH);
		
		if (value != null && !value.equals("") 
				&& (value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE)
						|| value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_FALSE)))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH
					+"' est incorrecte." ;							
		}
	
		// mail.smtp.host
		
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST
					+"' est incorrecte." ;							
		}
		
		// mail.smtp.port
				
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT, value);
			props.put(KEY_MAIL_SMTP_SOCKET_FACTORY_PORT, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT
					+"' est incorrecte." ;							
		}
		
		props.put(KEY_MAIL_SMTP_SOCKET_FACTORY_CLASS, VALUE_MAIL_SMTP_SOCKET_FACTORY_CLASS);
		
		if (!msg.equals("")) throw new EmailServiceException(msg);
		
		return(props);		
	}
	
	
	private Properties initNoSecuredProtocolMailConfig(Properties myAppConfig) 
			throws EmailServiceException
	{
		Properties props = new Properties();
		String msg = "";
		
		// mail.smtp.auth
		
		String value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH);
		
		if (value != null && !value.equals("") 
				&& (value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE)
						|| value.equalsIgnoreCase(EMAIL_CONFIG_VALUE_FALSE)))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH
					+"' est incorrecte." ;							
		}
	
		// mail.smtp.host
		
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST
					+"' est incorrecte." ;							
		}
		
		// mail.smtp.port
				
		value = myAppConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT);
		
		if (value != null && !value.equals(""))
		{
			props.put(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT, value);
		}
		else
		{
			msg = "Erreur dans le fichier de configuration." 
					+ " Valeur du paramètre '" + AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT
					+"' est incorrecte." ;							
		}
		
		if (!msg.equals("")) throw new EmailServiceException(msg);
		
		return(props);		
	}
	
	/*
	private String generateHtmlFooter()
	{
		String emailInfo = appConfig.getProperty(DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_EMAIL_INFO);
		String emailInfoLink = emailInfo == null || emailInfo.equalsIgnoreCase("") ? "" 
								: "<a ref=\"" + emailInfo + "\">Cliquer ici pour plus d'informations/<a>" ;
		
		String emailSiwise = appConfig.getProperty(DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_EMAIL_SIWISE);
		String emailSiwiseLink = emailSiwise == null || emailSiwise.equalsIgnoreCase("") ? "Silicon Wise"
								: "<a ref=\"" + emailSiwise + "\">Silicon Wise/<a>" ;
		
		String html = "<br/><span style=\"color:#555555;\"><hr/>Message g&eacute;n&eacute;r&eacute; automatiquement. Ne pas r&eacute;pondre." 
				+ "<hr/><b>TRIESYS:</b> Syst&egrave;me de Gestion du Transit Routier Inter Etat (TRIE)."
				+ "<br/>" + emailInfoLink
				+ "<br/>Conception: " + emailSiwiseLink 
				+ "</span>";
				
		return(html) ;
	}
	
	
	private String generateTextFooter()
	{
		String emailInfo = appConfig.getProperty(DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_EMAIL_INFO);
		String emailSiwise = appConfig.getProperty(DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_EMAIL_SIWISE);

		String text = "\n\n----------------------------------"
				+ "\nMessage généré automatiquement. Ne pas répondre."
				+ "\n----------------------------------"
				+ "\nSystème de Gestion du Transit Routier Inter Etat (TRIESYS)"
				+ "\nPour plus d'information: " + emailInfo
				+ "\nCnception: Silicon Wise (" + emailSiwise + ")";
				
		return(text) ;
	}
	
	
	
	/**
	 * Send email with with html content if recipent supports or text content otherwise  
	 * @param subject
	 * @param to
	 * @param cc
	 * @param textContent
	 * @param htmlContent
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws EmailServiceException Erreur dans la configuration des service email (voir fichier de configuration système) 
	 * 									ou erreur dans l'iitialisation des services email
	 * @throws IllegalArgumentException: destinataire to non defini ou incorrect
	 *
	 *
	public void sendEmail(String subject, InternetAddress[] to, InternetAddress[] cc, 
				String textContent, String htmlContent) 
			throws MessagingException, UnsupportedEncodingException, EmailServiceException, IllegalArgumentException
	{
		if (!isInitialized) initialise();
		
		// create mail session
		
		Session session = null ;
		
		if (emailServiceConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH).equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
		{
			logger.info("emailService::sendEmail: mailAuthUser"+mailAuthUser
					+ " mailAuthPassword=" + mailAuthPassword);
			
			session = Session.getInstance(emailServiceConfig,
				new javax.mail.Authenticator() {
				
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailAuthUser, mailAuthPassword);
					}
				});
		}
		else
		{
			session = Session.getDefaultInstance(emailServiceConfig);
		}
		
		session.setDebug(false); // debug mode
		
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
			
		// Set From: header field of the header.
		String from = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL); 
		String fromFullName = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_FULL_NAME);
		
		message.setFrom(fromFullName.equals("") 
				? new InternetAddress(from) : new InternetAddress(from, fromFullName));
		
		// add recipeints
		
		if (to == null) {
			
			String msg = "Aucun destinataire defini dans le courriel! (sujet:" + subject + ")" ;
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		
		message.addRecipients(Message.RecipientType.TO, to) ;
		
		if (cc != null) message.addRecipients(Message.RecipientType.CC, cc) ;
			
		// Set Subject: header field
		if (subject != null && !subject.equals("")) message.setSubject(subject);
		
		// create HTML content with text content alternative
		
		if (htmlContent != null && !htmlContent.equals("") )
		{
			Multipart multipart = new MimeMultipart("alternative");
			
			if (textContent != null && !textContent.equals(""))
			{
				// text content part
				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText(textContent + generateTextFooter());
				multipart.addBodyPart(textPart);
			}
			
			// html content part
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlContent + generateHtmlFooter(), "text/html");
			multipart.addBodyPart(htmlPart);
			
			message.setContent(multipart);
		}
		else
		{
			// text only content
			message.setText(textContent + generateTextFooter());
		}
		
		Transport.send(message);
		
		logger.info("emailService: courriel sujet: " + subject + " evoyé à : " + to.toString());
	}
	
	
	
	*/
	
	/**
	 * 
	 *  Modication des methodes faites  par Alzouma Moussa Mahamadou
	 *  Date: 11/08/2021
	 * 
	 */
	private String generateHtmlFooter()
	{
			
			//TODO A mettre en oeuvre pour recuperer ces deux parametres
		
		    String identifiantLegalPromoteur = "";
		    String emailPromoteur = "" ;
		    
			String html = "<h4 style=\"text-align: justify;\"><em><strong>Nous vous invitons &agrave; consulter votre espace de travail, "
						+ "sur la plateforme immobili&egrave;re, pour consultation et validation des conditions de credit bancaire immobilier"
						+ "accord&eacute;es au programme immobilier du promoteur "+ identifiantLegalPromoteur +".</strong></em></h4>\r\n" 
						+ "<p><em><strong>Vous pourriez contacter le promoteur par le mail suivant"+ emailPromoteur  +".</strong>"
						+ "</em><span style=\"color: #ff0000;\"><strong><em></em></strong></span></p>"
						+ "<p style=\"text-align: justify;\"><span style=\"color: #ff0000;\">"
						+ "<strong><em>Ceci est message automatique,veuillez &agrave; ne pas repondre.</em></strong></span></p>";
		
		   return html ;
	}
	
	private String generateTextFooter( )
	{
		
		//TODO A mettre en oeuvre pour recuperer ces deux parametres
		
		String text = "\n\n----------------------------------"
				+ "\nMessage généré automatiquement. Ne pas répondre."
				+ "\n----------------------------------"
				+ "\nSystème d'envoi de notification automatique" ;
				
					
		return(text) ;
		
	}
	
	/**
	 * Send email with with html content if recipent supports or text content otherwise  
	 * @param subject
	 * @param to
	 * @param cc
	 * @param textContent
	 * @param htmlContent
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws EmailServiceException Erreur dans la configuration des service email (voir fichier de configuration système) 
	 * 									ou erreur dans l'iitialisation des services email
	 * @throws IllegalArgumentException: destinataire to non defini ou incorrect
	 */
	public void sendEmail(String subject, InternetAddress[] to, InternetAddress[] cc, 
				String textContent, String htmlContent ) 
			throws MessagingException, UnsupportedEncodingException, EmailServiceException, IllegalArgumentException
	{
		if (!isInitialized) initialise();
		
		// create mail session
		
		Session session = null ;
		
		if (emailServiceConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH).equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
		{
			logger.info("emailService::sendEmail: mailAuthUser"+mailAuthUser
					+ " mailAuthPassword=" + mailAuthPassword);
			
			session = Session.getInstance(emailServiceConfig,
				new javax.mail.Authenticator() {
				
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailAuthUser, mailAuthPassword);
					}
				});
		}
		else
		{
			session = Session.getDefaultInstance(emailServiceConfig);
		}
		
		session.setDebug(false); // debug mode
		
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
			
		// Set From: header field of the header.
		String from = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL); 
		String fromFullName = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_FULL_NAME);
		
		message.setFrom(fromFullName.equals("") 
				? new InternetAddress(from) : new InternetAddress(from, fromFullName));
		
		// add recipeints
		
		if (to == null) {
			
			String msg = "Aucun destinataire defini dans le courriel! (sujet:" + subject + ")" ;
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		
		message.addRecipients(Message.RecipientType.TO, to) ;
		
		if (cc != null) message.addRecipients(Message.RecipientType.CC, cc) ;
			
		// Set Subject: header field
		if (subject != null && !subject.equals("")) message.setSubject(subject);
		
		// create HTML content with text content alternative
		
		if (htmlContent != null && !htmlContent.equals("") )
		{
			Multipart multipart = new MimeMultipart("alternative");
			
			if (textContent != null && !textContent.equals(""))
			{
				// text content part
				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText(textContent + generateTextFooter());
				multipart.addBodyPart(textPart);
			}
			
			// html content part
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlContent + generateHtmlFooter(), "text/html");
			multipart.addBodyPart(htmlPart);
			
			message.setContent(multipart);
		}
		else
		{
			// text only content
			message.setText(textContent + generateTextFooter());
		}
		
		Transport.send(message);
		
		logger.info("_627 emailService: courriel sujet: " + subject + " evoyé à : " + to.toString());
		
	}
	
	
	
	
	
	
	
	
	
	/* Send email with with html or text content and file attachments 
	 * @param subject
	 * @param to
	 * @param cc
	 * @param textContent
	 * @param htmlContent
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws EmailServiceException Erreur dans la configuration des service email (voir fichier de configuration système) 
	 * 									ou erreur dans l'iitialisation des services email
	 * @throws IllegalArgumentException: destinataire to non defini ou incorrect
	 */
	public void sendEmailWithFileAttachments(String subject, InternetAddress[] to, InternetAddress[] cc, 
				String content, boolean isHtml, File[] attachments) 
			throws MessagingException, EmailServiceException, IllegalArgumentException, IOException
	{
		
		if (!isInitialized) initialise();
		
		// create mail session
		
		Session session = null ;
		
		if (emailServiceConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH).equalsIgnoreCase(EMAIL_CONFIG_VALUE_TRUE))
		{
			logger.info("emailService::sendEmail: mailAuthUser"+mailAuthUser
					+ " mailAuthPassword=" + mailAuthPassword);
			
			session = Session.getInstance(emailServiceConfig,
				new javax.mail.Authenticator() {
				
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailAuthUser, mailAuthPassword);
					}
				});
		}
		else
		{
			session = Session.getDefaultInstance(emailServiceConfig);
		}
		
		session.setDebug(false); // debug mode
		
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
			
		// Set From: header field of the header.
		String from = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL); 
		String fromFullName = appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_MAIL_SENDER_FULL_NAME);
		
		message.setFrom(fromFullName.equals("") 
				? new InternetAddress(from) : new InternetAddress(from, fromFullName));
		
		// add recipeints
		
		if (to == null) {
			
			String msg = "Aucun destinataire defini dans le courriel! (sujet:" + subject + ")" ;
			logger.error(msg);
			throw new IllegalArgumentException(msg);
		}
		
		message.addRecipients(Message.RecipientType.TO, to) ;
		
		if (cc != null) message.addRecipients(Message.RecipientType.CC, cc) ;
			
		// Set Subject: header field
		if (subject != null && !subject.equals("")) message.setSubject(subject);
		
		// create HTML content with text content alternative
		
		Multipart multipart = new MimeMultipart();
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		
		if (isHtml) {
			messageBodyPart.setContent(content + generateHtmlFooter(), "text/html");
		}
		else {
			messageBodyPart.setContent(content + generateTextFooter(), "text");
		}
		
		multipart.addBodyPart(messageBodyPart);
		
		// attachments
		
		if (attachments != null) {
			
			for (File file : attachments) {
				
				MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.attachFile(file);
				multipart.addBodyPart(attachPart);
			}
		}
		
		message.setContent(multipart);
		
		Transport.send(message);
	}
	

}
