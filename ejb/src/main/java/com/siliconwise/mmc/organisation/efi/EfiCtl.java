package com.siliconwise.mmc.organisation.efi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.CreerModifierUnCompteUserCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahamadou
 *
 */
@Stateless
public class EfiCtl implements Serializable , EfiCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	EntityManager entityManager ;
	
	@Inject EfiDAOInterface  eFiDAO ;
	
	@Inject ActiverDesactiverUnEfiCtlInterface activerDescativerUnEfiCtl ;
	
	@Inject CreerModifierUnCompteUserCtlInterface  creerModifierUnCompteUserCtl ;
	
	@Inject CreerModifierUnEfiCtlInterface creerModifierUnEfiCtl ;
	
	@Inject EmailService emailService ;
	
	@Resource
	private EJBContext ejbContext;
	
	private Properties appConfig = AppConfigUtil.getAppConfig();
	
	private static transient Logger logger = LoggerFactory.getLogger(EfiCtl.class) ;

	@Override
	public List<EFi> tousLesEfis() {
	
		return eFiDAO.tousLesEfis();
		
	}

	
	
	@Override
	public Boolean confirmerCreationUnCompteEfi(
			        String  idEFi, 
			        boolean mustUpdateExistingNew,
			        String namedGraph,
			        boolean isFetchGraph, Locale locale,
			        User loggedInUser, List<NonLocalizedStatusMessage> msgList) {
		
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		//Mettre dans le contexte de persistence le compte EFi
		
		EFi rtn = EntityUtil
					.findEntityById(entityManager, idEFi, namedGraph, isFetchGraph, EFi.class);

		
		if (rtn == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
		   return false ;
		   
		}
		
		logger.info("_107 Compte EFi mis dans le contexte de persistence:"+rtn.toString()); //TODO A effacer
		
		//Activation du compte Efi 
		
		Boolean result = activerDescativerUnEfiCtl
				             .activerUnEfi(rtn.getId(), mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		
		if (!result) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_CONFIRMATION_CREATION_EFI_NON_EFFECTUE,
					 AppMessageKeys.CODE_TRADUCTION_CONFIRMATION_CREATION_EFI_NON_EFFECTUE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
			  
		}
		
		
		// Creation du compte de l'administrateur de l'organisation
		
		User user = new  User() ;
		
		user.setEmail(rtn.getEmailAdmin());
		user.setNom(rtn.getNomAdmin());
		user.setPrenom(rtn.getPrenomAdmin());
		
		
		
		//user.setVille(entity.getPays());
		
		User admin =  creerModifierUnCompteUserCtl
				        .CreerModifierUnCompteUser(user, mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		if (admin == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_CREATION_COMPTE_ADMINISTRATEUR_NON_EFFECTUE,
					 AppMessageKeys.CODE_TRADUCTION_CREATION_COMPTE_ADMINISTRATEUR_NON_EFFECTUE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		
			return false ;
			
		}
		
		logger.info("_141 Compte Administration Crée pour l'EFi:"+admin.toString());
		
		
	   // Envoi de notification de confirmation de creation de compte EFi
		
		String email = rtn.getEmail() != null 
							 ? rtn.getEmail()
							 : null ;
						 			
					
		//Verification de la variable mail
						 			
		if(email == null ||  email.isEmpty()) {
						
			String msg  = MessageTranslationUtil.translate(locale,
					EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE,
					EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE, 
							rtn.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
						
						
			return false ;
						
		}
						  
	    //Initialisation de l'objet de mail
					
		String subject  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_EFI, // venant du fichier
					 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_EFI, // Message par defaut
		rtn.getMsgVarMap()) ;


		//Vérifier si l'objet a été chargé
		
		if(subject == null || subject.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return false ;
			
		}
					
		//Addresse de l'organisation
				   
		InternetAddress to  ;
				    
		to = new InternetAddress() ;
		to.setAddress(email);
			    	
		InternetAddress[] toList = {to} ;
			
		//Adresse de copie carbone 
		
		/*
		InternetAddress cc = new InternetAddress();
					
		cc.setAddress("info@siliconwise.biz");*/
					
					
		InternetAddress[] ccList = {} ;
					
		String htmlContent = "" ;
					
		//Initialisation du contenu du message
				    
	    String textContent  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_EFI, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_EFI, // Message par defaut
		rtn.getMsgVarMap()) ;
	    
	    
	    if(textContent == null || textContent.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
		    return false ;
		    
		}
					
	    //Envoi de la notification
					
		try {
			
			//Appel à la méthode d'envoi de notification
				    	   
			emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
				    	   	
	    } catch (UnsupportedEncodingException | IllegalArgumentException 
	    		         | MessagingException | EmailServiceException e) {
							
	    	
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
					 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
		  
			return false ;
							
	   }
		
		return true;
	
	}


	@Override
	public EFi demandeCreationUnCompteEFi(
			      EFi entity, 
			      boolean mustUpdateExistingNew, 
			      String namedGraph,
			      boolean isFetchGraph, 
			      Locale locale, User loggedInUser, 
			      List<NonLocalizedStatusMessage> msgList) {
	
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		//Appel à la methode de creation d'un compte promoteur
		
		EFi rtn = creerModifierUnEfiCtl
				        .creerModifierUnEfi(entity, mustUpdateExistingNew, 
				            namedGraph, isFetchGraph, locale, loggedInUser, msgList);

		
		if (rtn == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			return null ;
			
		}
		
		
		//Envoie de notification au gestionnaire de la plateforme
	
		String email = rtn.getEmail() != null 
						 ? rtn.getEmail()
						 : null ;
			 			
		
		//Verification de la variable mail
					 			
		if(email == null ||  email.isEmpty()) {
					
			String msg  = MessageTranslationUtil.translate(locale,
					EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE,
					EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE, 
							entity.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
						
		    return null ;
					
		}
					  
				
		//Initialisation de l'objet de mail
				
		String subject = MessageTranslationUtil.translate(locale,
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CREATION_COMPTE_EFI, // venant du fichier
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CREATION_COMPTE_EFI, // Message par defaut
		entity.getMsgVarMap()) ;
		
		//Vérifier si l'objet a été chargé
		
		if(subject == null || subject.isEmpty()) {
					
			String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
							 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
							 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
					
			return null ;
					
		}		
			   
	
		//Recuperation de l'adresse mail du gestionniare de la plateforme à partir des fichiers de propriétés
		
		String emailGestionnairePlateforme =  appConfig.getProperty(AppConfigKeys.SYSTEM_CONFIG_KEY_GESTIONNAIRE_PLATEFORME_EMAIL);
					   
		InternetAddress to  ;
					    
		to = new InternetAddress() ;
		
		to.setAddress(emailGestionnairePlateforme != null ? emailGestionnairePlateforme : "");
				   	
		InternetAddress[] toList = {to} ;
		
		//Adresse de copie carbone du  promoteur 
				
		InternetAddress cc = new InternetAddress();
				
		cc.setAddress(email);
				
				
		InternetAddress[] ccList = {cc} ;
				
		String htmlContent = "" ;
				
		//Initialisation du contenu du message
			    
		String textContent  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CREATION_COMPTE_EFI_AU_GESTIONNAIRE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CREATION_COMPTE_EFI_AU_GESTIONNAIRE, // Message par defaut
		entity.getMsgVarMap()) ;
		
		
		//Verifier si le contenu du email a été chargé
		
		if(textContent == null || textContent.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
		    return null ;
		    
		  }
				
		
		//Envoi de la notification 
		
		try {
			
			//Appel à la methode d'envoiu de notification
			    	   
			emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
			    	   	
		} catch (UnsupportedEncodingException | IllegalArgumentException 
				         | MessagingException | EmailServiceException e) {
						
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
					 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
						
		}
		
		return rtn;
		
	}
	
}
