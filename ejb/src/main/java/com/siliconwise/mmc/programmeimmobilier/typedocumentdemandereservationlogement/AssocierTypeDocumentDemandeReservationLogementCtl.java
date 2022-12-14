package com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement;

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
public class AssocierTypeDocumentDemandeReservationLogementCtl implements Serializable , AssocierTypeDocumentDemandeReservationLogementCtlInterface {

	
	
	private static final long serialVersionUID = 1L;


	private static transient Logger logger = LoggerFactory.getLogger(AssocierTypeDocumentDemandeReservationLogementCtl.class) ;

	
	@Inject TypeDocumentDemandeReservationLogementDAOInterface typeDocumentDemandeReservationLogementDAO ;
	
	
	@Override
	public List<TypeDocumentDemandeReservationLogement> AssocierTypeDocumentDemandeReservationLogementList(
									List<TypeDocumentDemandeReservationLogement> entityList,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
				
		
				//Variable ModeFinancement Liste
		
				List<TypeDocumentDemandeReservationLogement> rtnList = new ArrayList<TypeDocumentDemandeReservationLogement>();
				
				
				
				for(TypeDocumentDemandeReservationLogement entity: entityList) {
				 
				 if ( entity != null ) {
					 
					 TypeDocumentDemandeReservationLogement rtn = AssocierUnTypeDocumentDemandeReservationLogement( entity, mustUpdateExistingNew,
						 												namedGraph, isFetchGraph, locale,  loggedInUser, msgList);
				 
				 
					 if(rtn == null) {
						 
						 String msg  = MessageTranslationUtil.translate(locale,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE, 
							 	entity.getMsgVarMap() ) ;
					  
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					     
					     return null ;
						 
					 }
					 
					  rtnList.add(rtn) ;
				 	} 
					
				}
				
				//retoure liste de mode de financements cr????e
				
				return rtnList;
				
				
}
	
	
	
public TypeDocumentDemandeReservationLogement 
             AssocierUnTypeDocumentDemandeReservationLogement(
            		TypeDocumentDemandeReservationLogement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	      	
	
	TypeDocumentDemandeReservationLogement rtn =
			                 typeDocumentDemandeReservationLogementDAO.validerEtEnregistrer(
					                entity, mustUpdateExistingNew, namedGraph, 
					                isFetchGraph, locale, loggedInUser, msgList); 
	
		
		return rtn ;
		
		
	}

	
}
