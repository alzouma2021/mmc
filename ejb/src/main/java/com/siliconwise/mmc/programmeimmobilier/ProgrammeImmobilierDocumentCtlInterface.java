package com.siliconwise.mmc.programmeimmobilier;

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
public interface ProgrammeImmobilierDocumentCtlInterface {
	
	
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
	public List<Document> rechercherDocumentListParProgrammeImmobilierParTypeDocument(
										String idProgrammeImmobilier,
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
	public ProgrammeImmobilierDocument validerEtEnregistrer(
			ProgrammeImmobilierDocument entity,
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
	public List<Document> rechercherDocumentListParIdProgrammeImmobilier(
									String idProgrammeImmobilier,
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
	public boolean supprimerDocumentParProgrammeImmobilier(
								String idDocument,
								boolean mustUpdateExistingNew,
								String namedGraph, boolean isFetchGraph, 
								Locale locale,  User loggedInUser,
								List<NonLocalizedStatusMessage> msgList);
	
	
}
