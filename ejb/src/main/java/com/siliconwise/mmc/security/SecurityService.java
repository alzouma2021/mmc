/**
 * 
 */
package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//import com.mashape.unirest.http.exceptions.UnirestException;
import com.siliconwise.common.Utility;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.LoginPasswordToken;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAOInterface;

import kong.unirest.*;

/**
 * @author TOSHIBA
 *
 */
@Stateless
public class SecurityService implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private static final int NEW_PASSWORD_LENGTH = 12 ;
	
	// @Inject private SessionBag sessionBag ;
	
	@Inject  UserDAOInterface userDAO ;
	
	//@Inject private EmailUtils emailUtils ;
	
	@Resource private EJBContext ejbContext;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	@com.siliconwise.mmc.common.entity.EntityInitializerQualifier(value=com.siliconwise.mmc.common.entity.EntityInitializerQualifier.TargetEntityEnum.ALL) 
	private com.siliconwise.mmc.common.entity.EntityInitializer entityInitializer ;
	
	@PersistenceContext private EntityManager entityManager ;

	//@Inject
	//private Subject subject ;

	/** Generate new password and send email to the user
	 * @param email
	 * @param msgList
	 * @return generated password
	 * @throws UnirestException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 */
	 
	public User generateAndSendNewPasword(
				String email, SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) 
		throws UnirestException, NoSuchAlgorithmException {
		
		Locale langue = SessionUtil.getLocale(sessionBag) ;
		
		if (email == null || email.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT,
					AppMessageKeys.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT, 
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;		
			
			return null ;
		}
		
		//logger.info(getClass().getName()+"::generateAndSendNewPasword::avant findUserByEmail=");
		//User user = userDAO.findUserByLogin(login, msgList) ;
		
		User rtn = new EntityUtil<User>().findSingleResultByFieldValues(
						entityManager, "userByEmailAndIfId", 
						new String[] {"email", "ifId"}, 
						new String[] {email, /*sessionBag.getIntermediaiareFinancierId()*/}, 
						"graph.user.person.nom-prenom-email", true, new User()) ;
		
		if (rtn == null) {
			
			String msg  = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_USER_NOT_FOUND_1V, 
					AppMessageKeys.CODE_TRADUCTION_USER_NOT_FOUND_1V, 
					new String[] {email != null ? email : ""}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;		
			
			return null ;
		}
		
		
				
       // String newPlainPassword = Utility.generateReadableRandomString(NEW_PASSWORD_LENGTH) ;
		
		// TODO crypter newPlainPassword
		
		//rtn.setCryptedPassword(Utility.encryptToSha256(newPlainPassword));
		
	   // rtn.setMustChangePassword(true);

		entityManager.flush();
		
		//boolean isOk = sendNewPasswordEmail(rtn, newPlainPassword, sessionBag, msgList) ;
		/*
		if (!isOk) {
			try {ejbContext.setRollbackOnly();}catch (Exception e) {}
			rtn = null ;
		}*/
		
		return rtn  ;
	}
	
	
	
	
	/**
	 * Send user newly generated password through email
	 * @param user
	 * @param password
	 * @throws UnirestException 
	 */
	/*
	private boolean sendNewPasswordEmail(User user, String plainPassword,
			SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) 
		throws  UnirestException {
		
		Locale langue = SessionUtil.getLocale(sessionBag) ;
		
		
		if (user == null)
		{ 
			String msg  = MessageTranslationUnit.dynamicTranslate(langue,
					User.CODE_TRADUCTION_NOT_DEFINED_MAIL_NOT_SENT,// venant du fichier
					User.CODE_TRADUCTION_NOT_DEFINED_MAIL_NOT_SENT, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
		}
		
		// Lire le conteu du mail à partir du modele
		
		Locale userLocale = user.getLanguage() != null && user.getLanguage().isEmpty() 
							? new Locale(user.getLanguage()) : SessionUtil.getLocale(null) ;
							
		String htmlContent = null ;
		
		try {
			htmlContent = AppConfigUtil.extractTranslationFileContent(
					AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_FILE_NAME,
					userLocale) ;
		}
		catch (Exception ex) {
			
			logger.error("_188 sendNewPasswordEmail :: Erreur pendant la lecture du fichier modele de mail de parametre de loggin " 
						+ " Fichier:"+AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_FILE_NAME
						+ " " + ex + ":" + ex.getMessage()+ " Cause: " + ex.getCause());
			
			ex.printStackTrace();
			
			htmlContent = null ;
		}
				
		if (htmlContent == null || htmlContent.isEmpty()) {
					
			String msg  = MessageTranslationUnit.dynamicTranslate(langue,
					User.CODE_TRADUCTION_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_FILE_LECTURE_ERREUR,// venant du fichier
					User.CODE_TRADUCTION_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_FILE_LECTURE_ERREUR, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

			logger.error("sendNewPasswordEmail() :: Erreur lecture fichier modele email. msg=" + msg);
			 return false ;
		}
		
		// Compute variable
		
		// Charger inf specifique de l'IF de l'utilisateur
		
		String idIf = user.getPersonne() != null && user.getPersonne().getIntermediaireFinancier() != null 
						? user.getPersonne().getIntermediaireFinancier().getId()
						: null ;
		
		InfoSpecifiqueIf infoSpecifiqueIf = (new EntityUtil<InfoSpecifiqueIf>(InfoSpecifiqueIf.class))
				.findSingleResultByFieldValues(entityManager, "infoSpecifiqueIfParIdIf", 
					new String[] {"idIf"}, new String[] {idIf}, 
					"graph.infospecifiqueif.urlHoteEtSiteName", true, new InfoSpecifiqueIf()) ;
		
		if (infoSpecifiqueIf == null ) {
			
			String msg  = MessageTranslationUnit.dynamicTranslate(langue,
					InfoSpecifiqueIf.CODE_TRADUCTION_NOT_FOUND,
					InfoSpecifiqueIf.CODE_TRADUCTION_NOT_FOUND, 
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

			logger.error("_211 sendNewPasswordEmail() :: " + msg + " Erreur anormale :: idIf="+idIf);
			
			return false ;
		}
		
		htmlContent = htmlContent
			.replaceAll(
				AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_VARIABLE_IF_SITE_NAME, 
				infoSpecifiqueIf.getSitename() != null ? infoSpecifiqueIf.getSitename() : "")
			.replaceAll(
				AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_VARIABLE_IF_SITE_URL, 
				infoSpecifiqueIf.getUrlHote() != null ? infoSpecifiqueIf.getUrlHote() : "")
			.replaceAll(
				AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_VARIABLE_FIRSTNAME, 
				user.getPersonne() != null ? user.getPersonne().getPrenom() : "")
			.replaceAll(
				AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_VARIABLE_LOGIN, 
				user.getLogin() != null ? user.getLogin() : "")
			.replaceAll(
				AppUtil.USER_LOGGIN_CREDENTIAL_EMAIL_TEMPLATE_VARIABLE_PASSWORD, 
				plainPassword) ;
	
		String subject = MessageTranslationUnit.dynamicTranslate(langue,
				AppUtil.CODE_TRADUCTION_EMAIL_SUBJECT_USER_LOGGIN_CREDENTIAL,
				AppUtil.CODE_TRADUCTION_EMAIL_SUBJECT_USER_LOGGIN_CREDENTIAL,
				new String[] {}) ; 
	
		emailUtils.envoyerEmail(subject, user.getLogin(), null, htmlContent);
		
		return true ;
		
	}	
*/

	/*public void createUser(
			Personne person, 
			List<NonLocalizedStatusMessage> msgList){
		
		createUser(person, null, msgList) ;
	}*/

	public User createUser(
			SessionBag sessionBag,
			List<NonLocalizedStatusMessage> msgList){
		
		Locale langue = SessionUtil.getLocale(sessionBag) ;
		
		// Créer un utilisateur
		
		User utilisateur = new User() ;
	//	utilisateur.setLogin(person.getEmail());
	//	utilisateur.setLanguage(langue == null ? AppUtil.APP_DEFAULT_LANGUAGE : langue.getLanguage());
	//	utilisateur.setPersonne(person);

		User rtn = this.validateAndSave(utilisateur, sessionBag, msgList) ;

		return rtn ;
	}
	
	/**
	 * validate user data , save the user and send credentia l passwrd if it is a new user
	 * @param entity
	 * @param msgList
	 * @return
	 * @throws Exception
	 */
	public User validateAndSave(
			User entity, SessionBag sessionBag,
			List<NonLocalizedStatusMessage> msgList){
		
		return validateAndSave(entity, false,
				null, true, sessionBag, msgList) ;
	}
	public User validateAndSave(
			User entity, boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, SessionBag sessionBag,
			List<NonLocalizedStatusMessage> msgList){
		
		String plainPassword = null ;
		Locale langue = SessionUtil.getLocale(sessionBag) ;
		
		
		if (entity == null) {
			
			 String msg  = MessageTranslationUtil.translate(langue,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}

		String userId = entity.getId() ;
		/*
		if (entity.getId() == null || entity.getId().equals("")) { // new user
			
			plainPassword = Utility.generateReadableRandomString(NEW_PASSWORD_LENGTH) ;
			
			try {
				entity.setCryptedPassword(Utility.encryptToSha256(plainPassword));
			} catch (NoSuchAlgorithmException ex) {
				
				String translatedMessage = MessageTranslationUnit.dynamicTranslate(langue,
						User.CODE_TRADUCTION_USER_ERREUR,// venant du fichier
						User.CODE_TRADUCTION_USER_ERREUR, // Message par defaut
						entity.getMessageVarList());

				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
				// Logger
				logger.error("_231 validateAndSave :: " + translatedMessage + " " + ex.getClass() + ex.getMessage() + " Cause: "+ex.getCause());
				ex.printStackTrace();
				
				return null ;
			}
			
			entity.setLanguage(AppUtil.APP_DEFAULT_LANGUAGE);
			// entity.setCryptedPassword(plainPassword);
		}
		
		User rtn = userDAO.validateAndSave(entity, mustUpdateExistingNew,
				namedGraph, isFetchGraph, sessionBag, msgList) ;
		
		if (userId == null) { // new user
			
			//logger.info("savedEntity.plainPassword = "+plainPassword+"\n"
			//		+ "savedEntity.login" + entity.getId());
			
			boolean isSent = false ;
			try {
				isSent = sendNewPasswordEmail(rtn, plainPassword, sessionBag, msgList) ;
			} 
			catch (UnirestException ex) {
				
				String translatedMessage = MessageTranslationUnit.dynamicTranslate(langue,
						User.CODE_TRADUCTION_USER_PASSWORD_MAIL_EXCEPTION,// venant du fichier
						User.CODE_TRADUCTION_USER_PASSWORD_MAIL_EXCEPTION,
						new String[] {});

				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
				
				logger.error("_356 validateAndSave :: " + translatedMessage 
						+ " " + ex + ":"+ ex.getMessage() + " Cause:"+ex.getCause());
				ex.printStackTrace();
				
				try {try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}} catch(Exception e){}
				return null ;
			}
			
			if (!isSent) return null ;
		}
		*/
		User rtn = null ;
		return rtn ;
	}
	
	/**
	 * Validate and save current user
	 * @param msgList
	 * @return
	 * @throws Exception
	
	public User validateAndSave(List<NonLocalizedStatusMessage> msgList)
			throws Exception {
		
		return validateAndSave(sessionBag.getLoggedInUser(), msgList) ;
		
	} */
	/*
	public boolean changePassword(String currentPassword, String newPassword, 
			Locale locale, List<NonLocalizedStatusMessage> msgList) 
		throws NoSuchAlgorithmException, IllegalArgumentException {
	
		String userId = sessionBag.getUserId() ;
		User user = userDAO.findUserbyId(userId, msgList) ;
		
		if (user == null) { 
			
			String translatedMessage = MessageTranslationUnit.dynamicTranslate(
					locale,
					User.CODE_TRADUCTION_USER_NOT_CONNECTED,
					User.CODE_TRADUCTION_USER_NOT_CONNECTED, 
					new String[] {} );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			return false ;
		}
		
		if (currentPassword == null || currentPassword.equals("")
				|| newPassword == null || newPassword.equals("")) {
			
			String translatedMessage = MessageTranslationUnit.dynamicTranslate(
					locale,
					User.CODE_TRADUCTION_CURRENT_OR_NEW_PASSWORD_NOT_DEFINED,
					User.CODE_TRADUCTION_CURRENT_OR_NEW_PASSWORD_NOT_DEFINED, 
					new String[] {} );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			return false ;
		}
		
		if (!userDAO.checkPassword(user, currentPassword)) { 
				
			String translatedMessage = MessageTranslationUnit.dynamicTranslate(
					locale,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT, 
					new String[] {} );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			return false ;
		}
		
		User savedUser = userDAO.findUserbyId(user.getId(), msgList) ;
		savedUser.setCryptedPassword(Utility.encryptToMD5(newPassword));
		
		entityManager.flush();
		
		return true ;
	}*/

	public User authentifier(LoginPasswordToken loginPasswordToken, 
			String ifId, Locale locale, 
			String namedGraphName, boolean isFetchHint,
			List<NonLocalizedStatusMessage> msgList) 
		throws NoSuchAlgorithmException {
		
		logger.info("authentifier :: entree");
		logger.info("authentifier :: ifId="+ifId+" loginPasswordToken="+loginPasswordToken);
		
		/*if (ifId == null || ifId.isEmpty()) {
			
			String msg = MessageTranslationUnit.dynamicTranslate(locale,
				 	IntermediaireFinancier.CODE_TRADUCTION_NOT_DEFINED ,
				 	IntermediaireFinancier.CODE_TRADUCTION_NOT_DEFINED, 
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}*/
		
		// crypt password
		
		String plainPassword = loginPasswordToken.getPassword() != null 
									? new String(loginPasswordToken.getPassword()) : null ;
		
		String cryptedPassword = plainPassword = null ;
									
	/*	String cryptedPassword = plainPassword != null 
											? Utility.encryptToSha256(plainPassword)
											: null ; */
											
		logger.info("authentifier :: login="+loginPasswordToken.getLogin()
					+" ifId="+ifId+" plainPassword="+plainPassword+" cryptedPassword="+cryptedPassword);
		
		User rtn = ifId != null
					? (new EntityUtil<User>(User.class)).findSingleResultByFieldValues(
							entityManager, "findUserByLoginAndIfIdAndCryptedPassword", 
							new String[] {"login", "ifId", "cryptedPassword"}, 
							new String[] {loginPasswordToken.getLogin(), ifId, cryptedPassword}, 
							namedGraphName, isFetchHint, new User()) 
					: (new EntityUtil<User>(User.class)).findSingleResultByFieldValues(
							entityManager, "findUserByLoginAndCryptedPasswordWhenIfIdIsNull", 
							new String[] {"login", "cryptedPassword"}, 
							new String[] {loginPasswordToken.getLogin(), cryptedPassword}, 
							namedGraphName, isFetchHint, new User()) ;
				
				//userDAO.findUserByLoginAndIfIdAndCryptedPassword(loginPasswordToken.getLogin(), 
				//ifId, cryptedPassword, namedGraphName, isFetchHint, msgList) ;

		logger.info("authentifier :: rtn="+rtn);

		return rtn ;
	}

	public boolean logout(SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) {
		
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		if (sessionBag == null) {
			
			String msg = MessageTranslationUtil.translate(locale,
				 	SessionBag.SESSION_NOT_DEFINED ,
				 	SessionBag.SESSION_NOT_DEFINED, 
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
		}
		
		sessionBag.setUserId(null); 
		
		return true ;
	}
	
	/*
	public Boolean shiroAuthentication(
			LoginPasswordToken loginPasswordToken, List<NonLocalizedStatusMessage> msgList)
				throws NoSuchAlgorithmException {
		
		logger.info(getClass().getName() 
				+"::shiroAuthentication ::  Entre :: Subject.isAuthenticated = " + subject.isAuthenticated());
		Boolean rtn = false ;
		
		UsernamePasswordToken token = 
				new UsernamePasswordToken(loginPasswordToken.getLogin(), loginPasswordToken.getPassword()) ;
					
		try {
			
			subject.login(token);	
			rtn = true ;
		} catch (UnknownAccountException uae) {
			
			String message = AppMessageKeys.CODE_UTILISATEUR_NON_VALIDE ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message));
			// logger.info("UnknownAccountException = "+ uae.getMessage());
			return rtn ;
			
		} catch (IncorrectCredentialsException  ice) {
			
			String message = AppMessageKeys.CODE_UTILISATEUR_NON_VALIDE ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message));
			// logger.info("IncorrectCredentialsException = "+ ice.getMessage());
			return rtn ;
			
		} catch (AuthenticationException  ae) {
			
			String message = AppMessageKeys.CODE_UTILISATEUR_NON_VALIDE ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message));
			// logger.info("AuthenticationException = "+ ae.getMessage());
			return rtn ;
		}
		
		
		return rtn ;
		
	}*/
	
	
	
	
	/**
	 * Date: 22/09/2021
	 * 
	 * Revue des methodes de la classe faite par Alzouma Moussa mahamadou
	 * 
	 */
	
	public String[] generateNewPasword() throws NoSuchAlgorithmException{
		
	    String newPlainPassword = Utility.generateReadableRandomString(NEW_PASSWORD_LENGTH) ;
	    
	    logger.info("_570 Nouveau mot de passe généré"+newPlainPassword); //TODO A effacer
		
		String newPlainPasswordEncrypted = Utility.encryptToMD5(newPlainPassword);
		
		logger.info("_574 Mot de passe généré encrypté="+newPlainPasswordEncrypted); //TODO A effacer
		
	    // rtn.setMustChangePassword(true);*/
		
		String[] Trtn = {newPlainPassword,newPlainPasswordEncrypted} ;
	
	
		return Trtn  ;
		
  }	
	
	

  public boolean changerPasswordUser(
		           String currentPassword, 
		           String newPassword, 
			       Locale locale,
			       SessionBag sessionBag,
			       List<NonLocalizedStatusMessage> msgList) 
		           throws NoSuchAlgorithmException, IllegalArgumentException {
	
	  
		String userId = sessionBag.getUserId() ;
		
		//Remettre l'entité dans le contexte de persistence
		
		User user =	EntityUtil
					.findEntityById(entityManager, userId, null, true, User.class);
		
		
		if (user == null) { 
			
			String translatedMessage = MessageTranslationUtil.translate(
					locale,
					User.CODE_TRADUCTION_USER_NOT_CONNECTED,
					User.CODE_TRADUCTION_USER_NOT_CONNECTED, 
					new String[] {} );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			
			return false ;
			
		}
		
		if (currentPassword == null || currentPassword.equals("")) {
			
			String translatedMessage = MessageTranslationUtil.translate(
					locale,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_DEFINED,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_DEFINED, 
					user.getMsgVarMap() );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			
			return false ;
		}
		
		if ( newPassword == null || newPassword.equals("")) {
			
			String translatedMessage = MessageTranslationUtil.translate(
					locale,
					User.CODE_TRADUCTION_NEW_PASSWORD_NOT_DEFINED,
					User.CODE_TRADUCTION_NEW_PASSWORD_NOT_DEFINED, 
					user.getMsgVarMap() );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			
			return false ;
			
		}
		
		
		//Verifivation du mot de passe actuel 
		
		boolean verifyPassword = userDAO.checkPassword(user, currentPassword,locale ,msgList) ;
		
		
		if (!verifyPassword) { 
				
			String translatedMessage = MessageTranslationUtil.translate(
					locale,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT,
					User.CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT, 
					user.getMsgVarMap() );
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			
			return false ;
			
		}
		
		
		User savedUser = EntityUtil
				          .findEntityById(entityManager, userId, null, true, User.class);
		
		
		savedUser.setMotDePasse(Utility.encryptToMD5(newPassword));
		
		
		entityManager.flush();
		
		
		return true ;
		
		
	}
	

}

