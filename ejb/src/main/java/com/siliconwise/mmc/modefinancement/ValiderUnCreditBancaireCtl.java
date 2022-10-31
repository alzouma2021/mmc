package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.LoggerFactory;

import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author sysadmin
 *
 */
@Stateless
public class ValiderUnCreditBancaireCtl implements Serializable,ValiderUnCreditBancaireCtlInterface {
	
	
	private static final long serialVersionUID = 1L;
	
	@Resource
	private EJBContext ejbContext;
	
	@Inject ValiderModeFinancementCtlInterface  validerModeFinancementCtl ;
	
	@Inject EmailService emailService ;
	
	Locale langue = SessionUtil.getLocale(null) ;
	
	@PersistenceContext
	EntityManager entityManager ;
	
	private static transient org.slf4j.Logger 
	                          logger = LoggerFactory.getLogger(ValiderUnCreditBancaireCtl.class) ;

	@Override
	public boolean validerUnCreditBancaire(
							 String idCreditBanciare, 
							 boolean mustUpdateExistingNew, 
							 String namedGraph,boolean isFetchGraph, 
							 Locale locale, User loggedInUser, 
							 List<NonLocalizedStatusMessage> msgList) {
	
		
		ModeFinancement rtn = validerModeFinancementCtl
				                .validerModeFinancement(idCreditBanciare, mustUpdateExistingNew, 
				                		 namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		if(rtn == null) return false ;
		
		//Envoyer de notification au promoteur correspondant
		
		//Recuperation de l'email du promoteur
		
	    String email = rtn != null 
						&& rtn.getProgrammeImmobilier() != null 
						&& rtn.getProgrammeImmobilier().getPromoteur() != null
						&& rtn.getProgrammeImmobilier().getPromoteur().getEmail() != null
							
						? rtn.getProgrammeImmobilier().getPromoteur().getEmail()
							
						: null ;
						
						
			//Vérification de la variable mail
			
			if(email == null ||  email.isEmpty()) {
									
				String msg  = MessageTranslationUtil.translate(locale,
							Promoteur.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE,
							Promoteur.CODE_TRADUCTION_PROMOTEUR_ADRESSE_MAIL_NON_DEFINIE, 
							rtn.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
									
					
			//return rtn ;
									
			 }
				
					
			//Initialisation de l'objet de mail
			
			String subject  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AU_PROMOTEUR, // venant du fichier
						 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AU_PROMOTEUR, // Message par defaut
					rtn.getMsgVarMap()) ;
			
			
			if(subject == null || subject.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 rtn.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ;
				
			}
			
			//Initialisation d'adresse du destinataire

			InternetAddress to = new InternetAddress();
					
			to.setAddress(email);
					
		    InternetAddress[] toList = {to} ;
					
			//Initialisation d'adresse de copie carbone : Adresse email de l'EFI
					
			InternetAddress cc = new InternetAddress();
			
			cc.setAddress(rtn.getCreditBancaire() != null 
					       && rtn.getCreditBancaire().getEfi() != null
					       && rtn.getCreditBancaire().getEfi().getEmail() != null
					       
						   ? rtn.getCreditBancaire().getEfi().getEmail()
								   
					       : "") ;
			
					
			InternetAddress[] ccList = {cc} ;
					
		    String htmlContent = "" ;
		    
			//Initialisation du contenu du message
		    
			String textContent  = MessageTranslationUtil.translate(langue,
					 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AU_PROMOTEUR, // venant du fichier
					 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AU_PROMOTEUR, // Message par defaut
					 rtn.getMsgVarMap()) ;
			
			
			 if(textContent == null || textContent.isEmpty()) {
					
					String msg  = MessageTranslationUtil.translate(locale,
							AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
							AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
							 rtn.getMsgVarMap()) ;
				    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
						
				    return false ;
				    
				}
	
			
			 //Try Catch
		
			 try {
				 
				  //Appel à la methode d'envoi de notification
				    	   
				  emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
				    	   	
			 } catch (UnsupportedEncodingException | IllegalArgumentException
									| MessagingException | EmailServiceException e) {
							
				   String msg  = MessageTranslationUtil.translate(locale,
						 	 	 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI,
						 	     AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI, 
						 	    rtn.getMsgVarMap() ) ;
				  
			       msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				  
				 // return null ;
			       
			 }
				        
		   return true ;
		
	  }

	
}
