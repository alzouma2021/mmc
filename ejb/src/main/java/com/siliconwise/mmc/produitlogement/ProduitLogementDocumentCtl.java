package com.siliconwise.mmc.produitlogement;

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
public class ProduitLogementDocumentCtl implements Serializable , ProduitLogementDocumentCtlInterface {

	
	
	private static final long serialVersionUID = 1L;

	@Resource private EJBContext ejbContext;
	
	@Inject ProduitLogementDocumentDAOInterface ProduitLogementDocumentDAO ;
	
	private static transient Logger logger = LoggerFactory.getLogger(ProduitLogementDocumentCtl.class) ;

	

	@Override
	public Document rechercherDocumentParProduitLogementParTypeDocument(
						String idProduitLogement, String typeDocument,
						boolean mustUpdateExistingNew, String namedGraph, 
						boolean isFetchGraph, Locale locale,
						List<NonLocalizedStatusMessage> msgList) {
					
		Document rtn =  ProduitLogementDocumentDAO
				          .rechercherDocumentParProduitLogementParTypeDocument(
					        		     idProduitLogement, typeDocument, 
					        		     mustUpdateExistingNew, namedGraph, 
					        		     isFetchGraph, locale, msgList);
		
		return rtn;
		
		
	}


	@Override
	public List<Document> rechercherDocumentListParProduitLogementParTypeDocument(
							String idProduitLogement,String typeDocument, 
							boolean mustUpdateExistingNew, String namedGraph, 
							boolean isFetchGraph, Locale locale,
							List<NonLocalizedStatusMessage> msgList) {
		
		
		List<Document> rtnList = ProduitLogementDocumentDAO
				                   .rechercherDocumentListParProduitLogementParTypeDocument(
					                		   idProduitLogement, typeDocument, 
					                		   mustUpdateExistingNew, namedGraph, 
					                		   isFetchGraph, locale, msgList) ;
		
				                   
		return rtnList;
		
		
	}



	@Override
	public ProduitLogementDocument validerEtEnregistrer(
			          ProduitLogementDocument entity, 
			          boolean mustUpdateExistingNew,
			          String namedGraph, boolean isFetchGraph, 
			          Locale locale, User loggedInUser,
			          List<NonLocalizedStatusMessage> msgList) {
		
		
		ProduitLogementDocument rtn = ProduitLogementDocumentDAO
				                        .validerEtEnregistrer(
				                        	entity, mustUpdateExistingNew, 
				                        	namedGraph, isFetchGraph, locale, 
				                        	loggedInUser, msgList) ;
		
		
		return rtn ;
		
		
	}



	@Override
	public List<Document> rechercherDocumentListParIdProduitLogement(
				               String idProduitLogement, 
				               boolean mustUpdateExistingNew, 
				               String namedGraph, 
				               boolean isFetchGraph, 
				               Locale locale,
				               List<NonLocalizedStatusMessage> msgList) {
		
		return ProduitLogementDocumentDAO
				.rechercherDocumentListParIdProduitLogement(
				   idProduitLogement, mustUpdateExistingNew, 
				   namedGraph, isFetchGraph, locale, msgList) ;
				  
		 
	}


	@Override
	public boolean supprimerDocumentParProduitLogement(
			         String idDocument, 
			         boolean mustUpdateExistingNew, 
			         String namedGraph,
			         boolean isFetchGraph, 
			         Locale locale, User loggedInUser,
			         List<NonLocalizedStatusMessage> msgList) {
		
		
		return ProduitLogementDocumentDAO
					.supprimerDocumentParProduitLogement(idDocument, mustUpdateExistingNew,namedGraph,
										isFetchGraph, locale, loggedInUser, msgList) ;
				
				
	}
	
	
	
}
