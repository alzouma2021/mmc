package com.siliconwise.mmc.demandereservationlogement.financement;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class EcheanceFinancementTemperamentDAO implements EcheanceFinancementTemperamentDAOInterface{


	
	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
			
	private static transient Logger logger = LoggerFactory.getLogger(EcheanceFinancementTemperamentDAO.class) ;
	
	@Override
	public boolean valider(EcheanceFinancementTemperament entity, 
					boolean mustUpdateExistingNew, String namedGraph,
					boolean isFetchGraph, Locale locale, 
					List<NonLocalizedStatusMessage> msgList) {
		
		
		
		//Verification financement temperament
		
		if(entity.getFinancementTemparement() == null ) {
			
			
			//TODO Ajout des mesages d'erreurs 
			
			return false ;
			
		}
	
		
		//Verifier si la designation est non nulle
		
		if(entity.getDesignation() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

			
			return false ;
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = new EntityUtil<EcheanceFinancementTemperament>().isEntityDuplicatedOrNotFound(
				entityManager, entity, mustUpdateExistingNew, "echeancefinancementtemperamentIdParDesignation", 
				new String[] {"designation"}, new String[] {entity.getDesignation()},
				AppMessageKeys.CODE_TRADUCTION_NOUVELLE_ENTITE_DUPLIQUEE, entity.getMsgVarMap(),
				AppMessageKeys.CODE_TRADUCTION_DUPLICATION_AVEC_ID_DIFFERENT, entity.getMsgVarMap(),
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, entity.getMsgVarMap(), 
				locale, msgList);
		
		
		if (isEntityDuplictedOrNotFound) return false ; 
		
		// verify that version is defined if entity id si not null
		
		if (entity.getId() != null && entity.getVersion() == null) {
			
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		

		// association
		
		try {
			
			
			EntityUtil<EcheanceFinancementTemperament> entityUtil = new EntityUtil<EcheanceFinancementTemperament>(EcheanceFinancementTemperament.class) ;
			
			
		    //ManyToOne Financement vers SimulationFinancementImmobilier
			 //Verifie si le produitlogmenet est non nul 
			
			 Map<String,String> msgVarMap = entity.getFinancementTemparement() == null 
										   || entity.getFinancementTemparement().getMsgVarMap() == null
										   ?  new HashMap<String,String>() : entity.getFinancementTemparement().getMsgVarMap() ;
			
			boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
				    entity, entity.getFinancementTemparement(), 
				    entity.getClass().getMethod("setFinancementTemparement", FinancementTemperament.class), null, true, 
			locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
			
			
			if (!isAttached) return false ;
			
			
		} 
		catch(Exception ex) {
			
			
			String msg  = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
					entity.getMsgVarMap()) ;
			
	        msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
	
			//logger.error("_1 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return false ;
		}
		
		// verification d'integrite complexes(règles metiers) 
		
		return true;
		
	}

	
	
	@Override
	public EcheanceFinancementTemperament validerEtEnregistrer(
					EcheanceFinancementTemperament entity,
					boolean mustUpdateExistingNew, String namedGraph, 
					boolean isFetchGraph, Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	
		
		if (entity == null) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}
		

		boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
								locale, msgList) ;
		
		
		if (!estValide) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ; 
		}
		
		
		//Persistence EchenaceFinancementTemperament
		
		EcheanceFinancementTemperament rtn = EntityUtil.persistOrMerge(
				entityManager, EcheanceFinancementTemperament.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
				locale, msgList);
		
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		} 
		
		return rtn ;
		
	}

}
