package com.siliconwise.mmc.produitlogement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.common.document.Document;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface ProduitLogementDocumentCtlInterface {
	
	
	
	
		
		/**
		 * 
		 * Retourne une image de consultation 
		 * avec ses metadatas et son contenu d'un produit logement donné
		 * 
		 */
		public Document rechercherDocumentParProduitLogementParTypeDocument(
									String idProduitLogement,
									String typeDocument,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale, List<NonLocalizedStatusMessage> msgList) ;
		
	
	
		
		/**
		 * @param idProduitLogement
		 * @param typeDocument
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param msgList
		 * @return
		 */
		public List<Document> rechercherDocumentListParProduitLogementParTypeDocument(
									String idProduitLogement,
									String typeDocument,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale, List<NonLocalizedStatusMessage> msgList) ;
		
		
		
		
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param loggedInUser
		 * @param msgList
		 * @return
		 */
		public ProduitLogementDocument validerEtEnregistrer(
				ProduitLogementDocument entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser,
				List<NonLocalizedStatusMessage> msgList);
		
		
		
		
		
		/**
		 * @param idProduitLogement
		 * @param typeDocument
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param msgList
		 * @return une liste de documents avec leurs metadatas seulement
		 */
		public List<Document> rechercherDocumentListParIdProduitLogement(
										String idProduitLogement,
										boolean mustUpdateExistingNew,
										String namedGraph, boolean isFetchGraph, 
										Locale locale, List<NonLocalizedStatusMessage> msgList) ;
		
		
		
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param loggedInUser
		 * @param msgList
		 * @return
		 */
		public boolean supprimerDocumentParProduitLogement(
									String idDocument,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale,  User loggedInUser,
									List<NonLocalizedStatusMessage> msgList);
		


}
