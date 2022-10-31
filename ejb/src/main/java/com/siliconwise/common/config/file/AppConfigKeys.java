/**
 * 
 */
package com.siliconwise.common.config.file;

/**
 * Application configuration file keys definition
 * @author GNAKALE Bernardin
 *
 */

public class AppConfigKeys {

    // Application parameters
	
	// Mettre le nom du fichier de 
	public static final String SYSTEM_CONFIG_FILE_NAME = "mmc.config.properties" ;
	
	public static final String CONFIG_KEY_SYSTEM_URL = "system.url" ;
	public static final String CONFIG_DEFAULT_VALUE_SYSTEM_URL = "http:/localhost:8080/mmc" ;
	public static final String SYSTEM_API_PATH = "/api" ;
	
	// Créer un fichier de traduction dynamique des references
	// 
	public static final String TRANSLATE_FILE_PREFIX = "translate_" ;
	public static final String TRANSLATE_STATIC_PREFIX = "translate_static" ;
	
	public static final String CONFIG_KEY_TRANSLATE_LOCALE = "translation.languages" ;
	public static final String TRANSLATE_LOCALE_LIST_SEPARATOR = "," ;
	public static final String TRANSLATE_LOCALE_LANGUAGE_COUNTRY_SEPARATOR = "-" ; // exemple fr-FR
	//public static final String TRANSLATE_FOLDER = "translation";
	public static final String CONFIG_KEY_TRANSLATE_FOLDER = "translation.folder.name";
	public static final String TRANSLATE_COUNTRY_FILE_NAME_PATTERN = "translate_pays:_locale:.properties" ;
	public static final String TRANSLATE_COUNTRY_FILE_NAME_DEFAULT = "translate_pays.properties" ;
	
	// Extention du fichier de traduction
	public static final String TRANSLATE_EXTENSIONS = "properties" ;

	
	// public static final String UPLOADED_FOLDER = "" ;
	public static final String UPLOADED_FOLDER_KEY = "upload.folder" ;
	
	public static final String SYSTEM_ENV_VAR_APPLICATION_HOME = "MMC_HOME";
	//  public static final String SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE = "c:\\nims";
	
	//sous un server de type linux
    public static final String SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE = "/home/sysadmin/bin/mmc";

    public static final String SYSTEM_CONFIG_KEY_GESTIONNAIRE_PLATEFORME_EMAIL = "email.gestionnaire.plateforme";
	 
	public static final String SYSTEM_CONFIG_KEY_INFO_EMAIL = "email.info";
	
	// email system parameters
	
	public static final String SYSTEM_CONFIG_KEY_MAIL_SMTP_HOST = "mail.smtp.host" ;
	public static final String SYSTEM_CONFIG_KEY_MAIL_SMTP_PORT = "mail.smtp.port" ;
	public static final String SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable" ;
	public static final String SYSTEM_CONFIG_KEY_MAIL_SMTP_STARTSSL_ENABLE = "mail.smtp.startssl.enable";
	public static final String SYSTEM_CONFIG_KEY_MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String SYSTEM_CONFIG_KEY_MAIL_USER = "mail.user";
	public static final String SYSTEM_CONFIG_KEY_MAIL_PASSWORD = "mail.password";
	public static final String SYSTEM_CONFIG_KEY_MAIL_SENDER_EMAIL = "mail.sender.email";
	public static final String SYSTEM_CONFIG_KEY_MAIL_SENDER_FULL_NAME = "mail.sender.full-name" ;

	// Temporary folder 
	public static final String SYSTEM_TEMPORARY_FOLDER_NAME = "tmp" ;
	// Création du nom d
	
	// alerte attachments folder
	public static final String SYSTTEM_CONFIG_KEY_FOLDER_ALERT_GEOLOCALIZATION = "dossier.alerte.geolocalisation" ;
	public static final String SYSTTEM_FOLDER_ALERT_GEOLOCALIZATION_DEFAULT_VALUE = "tmp\\alertes" ;
	
	// error document shown when a exception occure during printing pdf file generation process 
	public static final String SYSTEM_PRINT_PDF_DOCUMENT_ERROR_PATH = "print.pfd.document.error-path" ;
	public static final String SYSTEM_PRINT_XLS_DOCUMENT_ERROR_PATH = "print.xls.document.error-path" ;
	public static final String SYSTEM_PRINT_XLSX_DOCUMENT_ERROR_PATH = "print.xlsx.document.error-path" ;
	public static final String SYSTEM_PRINT_TXT_DOCUMENT_ERROR_PATH = "print.txt.document.error-path" ;
	
	//  Web service host  url
	
	// ypusign web app url
	public static final String SYSTEM_CONFIG_KEY_YOUSIGN_WEB_APP_URL = "yousign.webapp.url" ;
	
	// yousign api url
	public static final String SYSTEM_CONFIG_KEY_YOUSIGN_API_URL = "yousign.api.url" ;
	
//	public static final String SYSTEM_CONFIG_KEY_YOUSIGN_API_KEY = "yousign.api.key" ;
	
	// API maigun
	public static final String SYSTEM_CONFIG_KEY_MAILGUN_HOST_YOUSIGN_URL = "ws.host.url.mailgun" ;
	public static final String SYSTEM_CONFIG_KEY_MAILGUN_API_KEY = "ws.host.url.mailgun.api.key" ;
	public static final String SYSTEM_CONFIG_KEY_MAILGUN_SENDER_EMAIL = "ws.host.url.mailgun.mail.sender" ;
	
	/*
	public static final String SYSTEM_CONFIG_KEY_WEBSERVICE_HOST_WIALON_URL = "ws.host.url.wialon" ;
	
	public static final String SYSTEM_CONFIG_KEY_WEBSERVICE_HOST_WIALON_URL_SVC="ws.host.url.wialon.login.svc";
	public static final String SYSTEM_CONFIG_KEY_WEBSERVICE_HOST_WIALON_URL_PARAMS="ws.host.url.wialon.login.params";
	*/
	
	// ---- FRO?T END PROVIDED PATH AND URL

	// URL par defaut du front end
	// Valeur à utiliser s cela est nécessaire et que urlhoste de l'F n'est pas défini
	// dans les infos spécifiques
	public static final String CONFIG_KEY_FRONT_END_DEFAULT_HOST_URL = "front-end.host-url.default" ;
	
	// TODO A actualiser à la fin du projet avecla bonne url
	public static final String CONFIG_DEFAULT_FRONT_END_DEFAULT_HOST_URL = "http://v2-aziz.nafs.global" ;

	// Document validation and signature url provided by the front end
	// Real url is https://adresseIP:port/signature.front-end.url-pattern"
	// variable in url: :idCompteTitre: --> id compte titre, :idUser: --> id de l'utilisateur
	public static final String KEY_SIGNATURE_FRONT_END_URL_PATTERN = "signature.front-end.url-pattern" ;
	public static final String SIGNATURE_FRONT_END_URL_VARIABLE_ID_COMPTETITRE = ":idCompteTitre:" ;
	public static final String SIGNATURE_FRONT_END_URL_VARIABLE_ID_USER = ":idUser:" ;

	// Front end message display page (begin with /)
	// Path variable 
	// 	- type: "ERROR", "WARNING", "INFO"
	//	- message: to display
	// Exemple of path pattern /error?type=:type:&message=:message:
	public static final String CONFIG_KEY_FRONT_END_MESSAGE_PATH_PATTERN ="front-end.message-page.path.pattern" ;
	public static final String CONFIG_DEFAULT_FRONT_END_MESSAGE_PATH_PATTERN ="/message?type=:type:&message=:message:" ;
	public static final String CONFIG_KEY_FRONT_END_MESSAGE_PATH_VARIABLE_TYPE =":type:" ;
	public static final String CONFIG_KEY_FRONT_END_MESSAGE_PATH_VARIABLE_MESSAGE =":message:" ;
	
	
	// Confirmation
	
	// Pattern de l'url front end de confirmation de l'enregstrement des investisseur PP
	// http://adresseIP:port/investor.registration.confirmation.front-end.path-pattern
	// variable in url: :id: --> id investisseurpp, :code: --> code de confirmation
	// exemple pattern: backend/validation-enregistrement-pp/{id}/confirmer/{code}
	
	public static final String INVESTORPP_REGISTRATION_CONFIRMATION_FRONT_END_PATH_PATTERN = "investorpp.registration.confirmation.front-end.path-pattern" ;
	public static final String CONFIRMATION_URL_VARIABLE_INVESTOR_ID=":id:" ;
	public static final String CONFIRMATION_URL_VARIABLE_INVESTOR_CODE=":code:" ;
	
	
	//Confirmation Creation compte user
	//exemple baseUrl: http://192.168.241.245:8080/mmc/api
	public static final String CONFIRMATION_COMPTE_USER_BASE_URL = "confirmation.compte.user.base.url";
	public static final String CREATION_COMPTE_USER_CONFIRMATION_FRONT_END_PATH_PATTERN = "creation.compte.user.confirmation.front-end.path-pattern" ;
	public static final String CONFIRMATION_URL_VARIABLE_USER_CODE=":code:" ;
	
	public static final String CREATION_COMPTE_USER_CONFIRMATION_EMAILVARIABLE_NAME = ":name:" ;
	public static final String CREATION_COMPTE_USER_CONFIRMATION_EMAILVARIABLE_URL = ":url:" ;
	
	
	// Confoirmatio, de l'enregistrement de l'investisseur
	
	// Fichier modele de l'email de confirmation de l'enregistrement des investiseurs PP
	// Contenu utf8
	// variable {name}: nom / {url}: url de confirmation
	public static final String INVESTORPP_REGISTRATION_CONFIRMATION_EMAIL_TEMPLATE_FILE_NAME = "investorpp_registration_confirmation_email_template.txt" ;
	public static final String INVESTORPP_REGISTRATION_CONFIRMATION_EMAILVARIABLE_NAME = ":name:" ;
	public static final String INVESTORPP_REGISTRATION_CONFIRMATION_EMAILVARIABLE_URL = ":url:" ;

	// Fichier modele de l'email de reconfirmation envoyé lorsque le code de confirmation a expiré lors de la tentative de confirmation
	public static final String INVESTORPP_REGISTRATION_RECONFIRMATION_EMAIL_TEMPLATE_FILE_NAME = "investorpp_registration_reconfirmation_email_template.txt" ;

	
	// Code de message d'erreur
	
	public static final String MSG_INVESTORPP_REGISTRATION_CONFIRMATION_FRONT_END_PATH_PATTERN_NOT_DEFINED = "investorpp.registration.confirmation.front-end.path-pattern.not-defined" ;
	
	public static final String CREATION_COMPTE_USER_CONFIRMATION_FRONT_END_PATH_PATTERN_NOT_DEFINED = "creation.compte.user.confirmation.front-end.path-pattern.not-defined" ;
	// Libre office
	
	// libre office 1st socket port number, type: int, default: 2002
	public static final String KEY_LIBRE_OFFICE_SOCKET_PORT_FIRST = "libreoffice.socket-port.first" ;
	
	// libre office total number of socket ports number, type: int, default: 5
	public static final String KEY_LIBRE_OFFICE_SOCKET_PORT_TOTAL = "libreoffice.socket-port.total" ;

	
	// System script to kill libre office process. Should be an executable file
	public static final String KEY_LIBRE_OFFICE_KILL_BATCH_FILE_PATH = "libreoffice.kill-batch-file-path" ;
}
