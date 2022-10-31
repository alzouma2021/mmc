package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;


import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.document.Document;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ProgrammeImmobilierDocumentCtl implements Serializable , ProgrammeImmobilierDocumentCtlInterface {

	
	
	private static final long serialVersionUID = 1L;

	@Resource private EJBContext ejbContext;
	
	@Inject ProgrammeImmobilierDocumentDAOInterface programmeImmobilierDocumentDAO ;


	@Override
	public List<Document> rechercherDocumentListParProgrammeImmobilierParTypeDocument(
			               String idProgrammeImmobilier,
			               String typeDocument, 
			               boolean mustUpdateExistingNew, 
			               String namedGraph, boolean isFetchGraph,
			               Locale locale,List<NonLocalizedStatusMessage> msgList) {
		
		
		List<Document> rtnList = programmeImmobilierDocumentDAO
		.rechercherDocumentListParProgrammeImmobilierParTypeDocument(
				                	  idProgrammeImmobilier, 
				                	  typeDocument, 
				                	  mustUpdateExistingNew, 
				                	  namedGraph, 
				                	  isFetchGraph, 
				                	  locale, msgList);
		
		return rtnList;
		
		
	}
	

	@Override
	public ProgrammeImmobilierDocument validerEtEnregistrer(
			      ProgrammeImmobilierDocument entity,
			      boolean mustUpdateExistingNew, 
			      String namedGraph, boolean isFetchGraph, 
			      Locale locale, User loggedInUser,
			      List<NonLocalizedStatusMessage> msgList) {
		
		
		ProgrammeImmobilierDocument rtn =  programmeImmobilierDocumentDAO
				                            .validerEtEnregistrer(
				                              entity, mustUpdateExistingNew, 
				                              namedGraph, isFetchGraph, locale, 
				                              loggedInUser, msgList) ;
		
		
		
		return rtn;
		
		
		
	}
	
	
	
	@Override
	public List<Document> rechercherDocumentListParIdProgrammeImmobilier(
			              String idProgrammeImmobilier,
		                  boolean mustUpdateExistingNew, 
		                  String namedGraph, 
		                  boolean isFetchGraph, 
		                  Locale locale,
			              List<NonLocalizedStatusMessage> msgList) {
		
		
		List<Document> rtnList = programmeImmobilierDocumentDAO
				                  .rechercherDocumentListParIdProgrammeImmobilier(
				                		idProgrammeImmobilier, mustUpdateExistingNew, 
				                		namedGraph, isFetchGraph, locale, msgList) ;    
		

       return rtnList ;
       
       
	}


	@Override
	public boolean supprimerDocumentParProgrammeImmobilier(
			          String idDocument, 
			          boolean mustUpdateExistingNew, 
			          String namedGraph,
			          boolean isFetchGraph, 
			          Locale locale, User loggedInUser,
			          List<NonLocalizedStatusMessage> msgList) {
		
		
		return   programmeImmobilierDocumentDAO
				      .supprimerDocumentParProgrammeImmobilier(
				    	idDocument, mustUpdateExistingNew, namedGraph, 
				    	               isFetchGraph, locale, loggedInUser, msgList) ;
		
	
	}
	
	
	
}
