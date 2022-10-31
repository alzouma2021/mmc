package com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import javax.ejb.Stateless;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;

import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class AssocierCaracteristiqueDemandeReservationLogementCtl implements Serializable , AssocierCaracteristiqueDemandeReservationLogementCtlInterface {

	
	
	private static final long serialVersionUID = 1L;


	private static transient Logger logger = LoggerFactory.getLogger(AssocierCaracteristiqueDemandeReservationLogementCtl.class) ;

	@Inject CaracteristiqueDemandeReservationLogementDAOInterface caracteristiqueDemandeReservationLogementDAO ;
	
	
	@Override
	public List<CaracteristiqueDemandeReservationLogement> associerCaracteristiqueDemandeReservationLogementList(
									List<CaracteristiqueDemandeReservationLogement> entityList,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
				
		
				//Variable ModeFinancement Liste
		
				logger.info("_57  debut  creerModifierCaracteristiqueDemandeReservationLogementList entityList="+entityList.toString());   //TODO A effacer
		
				List<CaracteristiqueDemandeReservationLogement> rtnList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
				
				
				
				for(CaracteristiqueDemandeReservationLogement entity: entityList) {
					
					logger.info("_65  debut  boucle for creerModifierCaracteristiqueDemandeReservationLogementList entityList="+entity.toString());   //TODO A effacer
				 
				 if ( entity != null ) {
					 
				  logger.info("_69  Appel de la methode  creerModifierUneCaracteristiqueDemandeReservationLogement" );   //TODO A effacer
					 
				  CaracteristiqueDemandeReservationLogement rtn = associerUneCaracteristiqueDemandeReservationLogement( entity, mustUpdateExistingNew,
						 					namedGraph, isFetchGraph, locale,  loggedInUser, msgList);
				 
				 
					 if(rtn == null) {
						 
						 String msg  = MessageTranslationUtil.translate(locale,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE, 
							 	entity.getMsgVarMap() ) ;
					  
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					     
					     return null ;
						 
					 }
					 
					 logger.info("_86  Caracteristique demande reservation logement crééé="+rtn.toString() );   //TODO A effacer 
					 
					  rtnList.add(rtn) ;
					  
				 	} 
					
				}
				
				//retoure liste de mode de financements créée
				
				return rtnList;
				
				
}
	
	
	
public CaracteristiqueDemandeReservationLogement 
             associerUneCaracteristiqueDemandeReservationLogement(
            		CaracteristiqueDemandeReservationLogement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	      	
	
		CaracteristiqueDemandeReservationLogement rtn =
			                 caracteristiqueDemandeReservationLogementDAO.validerEtEnregistrer(
					                entity, mustUpdateExistingNew, namedGraph, 
					                isFetchGraph, locale, loggedInUser, msgList); 
	
		
		return rtn ;
		
		
	}

	
}
