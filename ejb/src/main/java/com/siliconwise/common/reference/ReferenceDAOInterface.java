package com.siliconwise.common.reference;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.modefinancement.TypeFinancement;
import com.siliconwise.mmc.produitlogement.OperateurCritere;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface ReferenceDAOInterface {
	
	
	public List<ReferenceFamille> trouverToutesLesFamilles() ;
	
	
	
	/**
	 * @param designationFamille
	 * @return
	 */
	public List<Reference> trouverReferencesParDesignationFamilleRargeesParDesignationCroissant(
			String designationFamille);
	
	
	/**
	 * @param critere
	 * @return
	 */
	public List<ReferenceFamille> trouverReferenceFamille(CritereRechercheReference critere);
	
	
	
	/**
	 * @param familleDesignation
	 * @return
	 */
	public List<Reference> trouverToutesLesReferenceParDesignationFamilleReference(String familleDesignation);
	
	
	
	/**
	 * @param designation
	 * @return
	 */
	public Reference trouverReferenceParDesignationReference(String designation);
	
	
	
	/**
	 * @param idRef
	 * @param msgList
	 * @param langue
	 * @return
	 */
	public Reference trouverUneReferenceParSonId(String idRef, 
			List<NonLocalizedStatusMessage> msgList,
			Locale langue);
	
	
	
	/**
	 * @param idRef
	 * @return
	 */
	public Reference trouverUneReferenceParSonId(String idRef) ;
	
	
	
	/**
	 * @param searchIdFamille
	 * @param msgList
	 * @param langue
	 * @return
	 */
	public List<Reference> trouverToutesLesReferenceParIdFamille(
			String searchIdFamille, 
			List<NonLocalizedStatusMessage> msgList,
			Locale  langue);
	
	
	
	/**
	 * @return
	 */
	public List<OperateurCritere> tousLesOperateursCriteres();
	
	
	
	/**
	 * @return
	 */
	public List<TypeFinancement> tousLesTypeFinancements();
	
	
}
