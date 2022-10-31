package com.siliconwise.common.reference;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.modefinancement.TypeFinancement;
import com.siliconwise.mmc.produitlogement.OperateurCritere;

/***
 * @author Alzouma Moussa Mahamadou
 */

@Stateless
public class ReferenceCtl implements Serializable , ReferenceCtlInterface{
	
	
	
		private static final long serialVersionUID = 1L;
		
		
		@Inject ReferenceDAOInterface referenceDAO ;

		@Override
		public List<ReferenceFamille> trouverToutesLesFamilles() {
			
			return referenceDAO.trouverToutesLesFamilles();
			
		}
	
		@Override
		public List<Reference> trouverReferencesParDesignationFamilleRargeesParDesignationCroissant(
				String designationFamille) {
			
			
			return referenceDAO.trouverReferencesParDesignationFamilleRargeesParDesignationCroissant(designationFamille);
			
		}
	
		@Override
		public List<ReferenceFamille> trouverReferenceFamille(CritereRechercheReference critere) {
			
			
			return referenceDAO.trouverReferenceFamille(critere);
		}
	
		@Override
		public List<Reference> trouverToutesLesReferenceParDesignationFamilleReference(String familleDesignation) {
		
			
			return referenceDAO.trouverToutesLesReferenceParDesignationFamilleReference(familleDesignation);
		}
	
		@Override
		public Reference trouverReferenceParDesignationReference(String designation) {
			
			
			return referenceDAO.trouverReferenceParDesignationReference(designation);
		}
	
		@Override
		public Reference trouverUneReferenceParSonId(String idRef, List<NonLocalizedStatusMessage> msgList, Locale langue) {
			
			
			return referenceDAO.trouverUneReferenceParSonId(idRef, msgList, langue);
		}
	
		@Override
		public Reference trouverUneReferenceParSonId(String idRef) {
			
			
			return  referenceDAO.trouverUneReferenceParSonId(idRef);
		}
	
		@Override
		public List<Reference> trouverToutesLesReferenceParIdFamille(String searchIdFamille,
				List<NonLocalizedStatusMessage> msgList, Locale langue) {
			
			return referenceDAO.trouverToutesLesReferenceParIdFamille(searchIdFamille, msgList, langue);
		}
		
		

		@Override
		public List<OperateurCritere> touslesOperateursCriteres() {
			
			
			return referenceDAO.tousLesOperateursCriteres();
			
		}

		@Override
		public List<TypeFinancement> tousLesTypeFinancements() {
			
			
			return referenceDAO.tousLesTypeFinancements();
		}
		
		
		

}
