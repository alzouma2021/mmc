package com.siliconwise.common.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.TypeFinancement;
import com.siliconwise.mmc.produitlogement.OperateurCritere;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ReferenceDAO implements ReferenceDAOInterface{

		
		@PersistenceContext 
		private EntityManager entityManager ;
	
		private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		
		@SuppressWarnings("unchecked")
		@Override
		public List<OperateurCritere> tousLesOperateursCriteres() {
			
			List<OperateurCritere> rtnList = new ArrayList<>() ;
			
			try {
				
				rtnList = (List<OperateurCritere>) entityManager
					.createNamedQuery("tousLesOperateurCriteres")
					.getResultList();
				
			} catch (NoResultException e) {

				return null ;
			}
			
			return rtnList ;
			
		}
		
		
	
		@SuppressWarnings("unchecked")
		@Override
		public List<TypeFinancement> tousLesTypeFinancements() {

			List<TypeFinancement> rtnList = new ArrayList<>() ;
			
			try {
				
				rtnList = (List<TypeFinancement>) entityManager
					.createNamedQuery("tousLesTypeFinancements")
					.getResultList();
				
			} catch (NoResultException e) {

				return null ;
			}
			
			return rtnList ;
			
		}


		@SuppressWarnings("unchecked")
		@Override
		public List<ReferenceFamille> trouverToutesLesFamilles() {
			
			List<ReferenceFamille> rtnListe = new ArrayList<>() ;
			try {
				
				rtnListe = (List<ReferenceFamille>) entityManager
					.createNamedQuery("toutesLesFamilles")
					.getResultList();
				
			} catch (NoResultException e) {

				return null ;
			}
			
			return rtnListe ;
		}
	
		@Override
		public List<Reference> trouverReferencesParDesignationFamilleRargeesParDesignationCroissant(
				String designationFamille) {
			
			
			return null;
		}
	
		@SuppressWarnings("unchecked")
		@Override
		public List<ReferenceFamille> trouverReferenceFamille(CritereRechercheReference critere) {
			
			final String SQL_SELECT = "SELECT r " ;
			final String SQL_FROM = "Reference r , ReferenceFamille rf" ;
		
			final String SQL_WHERE_DESIGNATION = "(r.designation Like = :searchDesignation) ";
			
			final String SQL_ORDERBY = "ORDER BY r.designation ASC " ;
			
			// aucun critere defini. ne rien retourné
			
			if (critere == null) return new ArrayList<>();
			
			String sqlWhere = ""; 
			
			if (critere.getSearchDesignation() != null && !critere.getSearchDesignation().equals("")){
				sqlWhere += (sqlWhere.equals("") ? "" : " AND ") + SQL_WHERE_DESIGNATION ;
			}
			
		
			// CONSTRUCTION DE LA REQUETTE
			
			String sql = SQL_SELECT + " FROM " + SQL_FROM + " " 
							+ (sqlWhere.equals("") ? "" : " WHERE " + sqlWhere) 
							+ " " + SQL_ORDERBY ;
			
			Query query = entityManager.createQuery(sql);
		
			// PARAMETRES 
			 
			if (critere.getSearchDesignation() != null && !critere.getSearchDesignation().equals("")) {
				query.setParameter("designation", critere.getSearchDesignation() + "%") ;
			}	
			
			return query.getResultList() ;
		}
	
		@Override
		public List<Reference> trouverToutesLesReferenceParDesignationFamilleReference(String familleDesignation) {
			
			
			return null;
		}
	
		@Override
		public Reference trouverReferenceParDesignationReference(String designation) {
			
			Reference rtn = null ;
			
			try {
				
				rtn = (Reference) entityManager.createNamedQuery("referenceParDesignationReference")
						.setParameter("designation", designation)
						.getSingleResult();
				
			} catch (NoResultException e) {
				
				return null ;
			}
			
			
			return rtn ;
		}
	
		
		@Override
		public Reference trouverUneReferenceParSonId(String idRef, List<NonLocalizedStatusMessage> msgList, Locale langue) {
			
				Reference rtn = null ;
				try {
					
					rtn = entityManager.find(Reference.class, idRef) ;
					
				} catch (NoResultException e) {
					
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, e.getMessage())) ;
					return null ;
				}
				
				/*
				// Gerer la traduction
				String designation = MessageTranslationUtil.translate(
						langue, 
						rtn.getCodeTrDesignation(),
						rtn.getDesignation(),
						rtn.getMessageVarList()) ;
				
				rtn.setDesignation(designation);
				
				String description = MessageTranslationUtil.translate(
						langue, 
						rtn.getCodeTrDescription(),
						rtn.getDescription() ,
						rtn.getMessageVarList()) ;
				rtn.setDescription(description);*/
				
				return rtn ;
		}
	
		@Override
		public Reference trouverUneReferenceParSonId(String idRef) {
			
				Reference rtn = null ;
				
				if(idRef != null)
					rtn = entityManager.find(Reference.class, idRef) ;
				
				return rtn ;
		}
	
		@SuppressWarnings("unchecked")
		@Override
		public List<Reference> trouverToutesLesReferenceParIdFamille(
								String searchIdFamille,
								List<NonLocalizedStatusMessage> msgList, 
								Locale langue) {
						
			
			List<Reference> referenceListe =  new ArrayList<Reference>() ;
			
			try {
				
				referenceListe = (List<Reference>) entityManager
						.createNamedQuery("toutesLesReferenceParIdFamille")
						.setParameter("searchIdFamille", searchIdFamille)
						.getResultList() ;
				
				
			} catch (NoResultException e) {
				
				return null ;
				 
			}
		
			/*
			// Gestion de la traduction
			
			List<Reference> rtnList =  new ArrayList<>() ;
			
			for(Reference reference : referenceListe) {
				
				Reference rtn = new Reference() ;
				rtn.setId(reference.getId());
				rtn.setCode(reference.getCode());
				
				String designation = MessageTranslationUtil.translate(
						langue, 
						reference.getCodeTrDesignation(),
						reference.getDesignation(),
						new String[] {}) ;
				
				
				rtn.setDesignation(designation);
				
				String description =  MessageTranslationUtil.translate(
						langue, 
						reference.getCodeTrDescription(),
						reference.getDescription() ,
						new String[] {}) ;
				rtn.setDescription(description);
				
				rtnList.add(rtn) ;
				
			}*/
			
			return referenceListe ;
			
			
		}
	
}
