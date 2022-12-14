package com.siliconwise.mmc.organisation.promoteur;

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

import com.siliconwise.common.Pays;
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
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class PromoteurCtl implements Serializable , PromoteurCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
	
	@PersistenceContext
	EntityManager entityManager ;
		
	@Inject PromoteurDAOInterface promoteurDAO ;
	
	@Inject ActiverDesactiverUnPromoteurCtlInterface activerDescativerUnPromoteurCtl ;
	
	@Inject CreerModifierUnCompteUserCtlInterface creerModifierUnUserCtl ;
	
	@Inject CreerModifierUnPromoteurCtlInterface creerModifierUnPromoteurCtl ;
	
	@Inject EmailService emailService ;
	
	private Properties appConfig = AppConfigUtil.getAppConfig();
	

	@Override
	public Boolean confirmerCreationUnComptePromoteur(
			            String idPromoteur, 
			            boolean mustUpdateExistingNew,
			            String namedGraph, 
			            boolean isFetchGraph, 
			            Locale locale, User loggedInUser,
			            List<NonLocalizedStatusMessage> msgList) {
		
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		//Mettre dans le contexte de persistence le compte Promoteur
		
		Promoteur rtn = EntityUtil
					.findEntityById(entityManager, idPromoteur, namedGraph, isFetchGraph, Promoteur.class);

			
		if ( rtn == null ) {
		
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		//Activation du compte promoteur 
		
		Boolean result = activerDescativerUnPromoteurCtl
				             .activerUnPromoteur(rtn.getId(), mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		 
		
		if (!result) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_CONFIRMATION_CREATION_PROMOTEUR_NON_EFFECTUE,
					 AppMessageKeys.CODE_TRADUCTION_CONFIRMATION_CREATION_PROMOTEUR_NON_EFFECTUE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			  
		}
		
		
		// Creation du compte de l'administrateur de l'organisation
		
		User user = new  User() ;
		
		user.setEmail(rtn.getEmailAdmin());
		user.setNom(rtn.getNomAdmin());
		user.setPrenom(rtn.getPrenomAdmin());
	
		
		User admin =  creerModifierUnUserCtl.CreerModifierUnCompteUser(user, mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		
		if (admin == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_CREATION_COMPTE_ADMINISTRATEUR_NON_EFFECTUE,
					 AppMessageKeys.CODE_TRADUCTION_CREATION_COMPTE_ADMINISTRATEUR_NON_EFFECTUE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			  
		}
		
		
	   // Envoi de notification de confirmation de creation de compte Promoteur
		
		String email = rtn.getEmail() != null 
							 ? rtn.getEmail()
							 : null ;
						 			
					
		//Verification de la variable mail
						 			
		if(email == null ||  email.isEmpty()) {
						
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE, 
					rtn.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
						
			return false ;
						
		}
						  
					
	    //Initialisation de l'objet de mail
					
		String subject  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_PROMOTEUR, // venant du fichier
					 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_PROMOTEUR, // Message par defaut
			   rtn.getMsgVarMap()) ;
		
		
		//V??rifier si l'objet a ??t?? charg??
		
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
			
		//Adresse de copie carbone du  promoteur du programme immobilier 
					
		 InternetAddress[] ccList = {} ;
					
		 String htmlContent = "" ;
					
		 //Initialisation du contenu du message
				    
	     String textContent  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_AU_PROMOTEUR, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_AU_PROMOTEUR, // Message par defaut
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
					
					
		 try {
			 
			 //Appel ?? la methode d'envoi de notification
				    	   
			 emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
				    	   	
		  } catch (UnsupportedEncodingException | IllegalArgumentException | MessagingException | EmailServiceException e) {
							
				
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
	public Promoteur demandeCreationUnComptePromoteur(
			           Promoteur entity, 
			           boolean mustUpdateExistingNew,
			           String namedGraph, 
			           boolean isFetchGraph, 
			           Locale locale, User loggedInUser,
			           List<NonLocalizedStatusMessage> msgList) {
		
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		//Appel ?? la methode de creation d'un compte promoteur
		
		Promoteur rtn = creerModifierUnPromoteurCtl
				            .creerModifierUnPromoteur(entity, mustUpdateExistingNew, 
				            		namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		
		if (rtn == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return null ;
			
		}
		
		
		//Envoie de notification au gestionnaire de la plateforme
	
		String email = rtn.getEmail() != null 
						 ? rtn.getEmail()
						 : null ;
			 			
						 
		//Verification de la variable mail
					 			
		if(email == null ||  email.isEmpty()) {
					
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE, 
							entity.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
						
		    return null ;
					
		}
					  
				
		//Initialisation de l'objet de mail
				
		String subject = MessageTranslationUtil.translate(locale,
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CREATION_COMPTE_PROMOTEUR, // venant du fichier
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CREATION_COMPTE_PROMOTEUR, // Message par defaut
		entity.getMsgVarMap()) ;
				
		if(subject == null || subject.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 rtn.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
			
			
		}
		
		//Recuperation de l'adresse mail du gestionniare de la plateforme ?? partir des fichiers de propri??t??s
		
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
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CREATION_COMPTE_PROMOTEUR_AU_GESTIONNAIRE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CREATION_COMPTE_PROMOTEUR_AU_GESTIONNAIRE, // Message par defaut
		entity.getMsgVarMap()) ;
		
		
		if(textContent == null || textContent.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 rtn.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
			
			
		}
				
	   //Envoi de notification au promoteur
		
	   try {
			
			//Appel ?? la methode d'envoie de notification
			    	   
			emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
			    	   	
		} catch (UnsupportedEncodingException | IllegalArgumentException | MessagingException | EmailServiceException e) {
						
				
			  String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
						 msgVarMap) ;
			  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			  
			  try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			  
			  return null ;
						
		}
		
		return rtn ;
		
	}



	
	@Override
	public List<Promoteur> tousLesPromoteurs() {
		
		List<Promoteur> rtnList = promoteurDAO.tousLesPromoteurs() ;
		
		return rtnList ;
		
		
	}
	
	
	
}
