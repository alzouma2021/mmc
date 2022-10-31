package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ValiderModeFinancementCtl implements Serializable,ValiderModeFinancementCtlInterface {
	
	
	private static final long serialVersionUID = 1L;
	
	@Inject ModeFinancementDAOInterface modeFinancementDAO ;
	
	@Inject EmailService emailService ;
	
	private static transient Logger logger = LoggerFactory.getLogger(ValiderModeFinancementCtl.class) ;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public List<ModeFinancement> validerOuNotifierModeFinancementList(
										List<ModeFinancement> entityList,
										boolean mustUpdateExistingNew,
										ProgrammeImmobilier programme, 
										String namedGraph, boolean isFetchGraph, 
										Locale locale, User loggedInUser,
										List<NonLocalizedStatusMessage> msgList) {
				
		
		//Variable ModeFinancement Liste
		
		List<ModeFinancement> rtnList = new ArrayList<ModeFinancement>();
		
		//Boucle d'envoi de notification aux EFis non partenaires
		
		for(ModeFinancement entity: entityList) {
		 
		 if(entity != null) {
			 
			 if (entity.getTypeFinancement().isEstvalideParEfi()) {
				  
				//Envoie de notification à l'EFI partenaire pour validation des conditions portant sur le crédit bancaire
				 
				 boolean rtn = notifierModeFinancement(entity,  mustUpdateExistingNew, 
							 	namedGraph, isFetchGraph, locale,  loggedInUser, msgList) ;
		        
				 
					if(!rtn) {
							 
					   String msg  = MessageTranslationUtil.translate(locale,
								 	 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI,
								 	 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI, 
								 	entity.getMsgVarMap() ) ;
						  
					   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					     
					 }
					
	
			    }else {
				
				 //Appel à la methode de validation mode de financement
			    	
				 ModeFinancement rtn = validerModeFinancement(entity.getId(), mustUpdateExistingNew,
										    namedGraph, isFetchGraph, locale,  loggedInUser, msgList);
				 
				 		
					 if(rtn == null) {
						 
						 String msg  = MessageTranslationUtil.translate(locale,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_VALIDE,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_VALIDE, 
							 	entity.getMsgVarMap() ) ;
					  
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						 
					 }
					 
					 rtnList.add(rtn) ;
					 
			 	}
			 
		 	}
		 
		}
		
		//retourne liste des mode de financements validés ou notifiés
		
		return rtnList;
		
    }


	@Override
	public boolean notifierModeFinancement(
					ModeFinancement entity, boolean mustUpdateExistingNew, 
					String namedGraph,boolean isFetchGraph, 
					Locale locale, User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList) {
		
		
			boolean rtn = modeFinancementDAO.validerEtNotifier(entity, mustUpdateExistingNew, 
									 namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			
			return rtn ;
		
		
		}
	
	
	@Override
	public ModeFinancement validerModeFinancement(
							String idModeFinancement,
							boolean mustUpdateExistingNew, 
							String namedGraph,boolean isFetchGraph, 
							Locale locale, User loggedInUser, 
							List<NonLocalizedStatusMessage> msgList) {
			
			ModeFinancement rtn = modeFinancementDAO.validerEtConfirmer(idModeFinancement,mustUpdateExistingNew, 
											 			namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			
			return rtn;
		
			
		}

}
