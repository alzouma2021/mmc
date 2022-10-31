package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ModeFinancementDAO implements Serializable , ModeFinancementDAOInterface {
	
	
	private static final long serialVersionUID = 1L;
	
	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	private static transient Logger logger = LoggerFactory.getLogger(ModeFinancementDAO.class) ;
	
	@Inject CreditBancaireDAOInterface creditBancaireDAO ;
	
	@Inject PallierComptantSurSituationDAOInterface pallierComptantSurSituationDAO ;
	
	@Inject TemperamentDAOInterface temperamentDAO ;
	
	@Inject TiersCollecteurDAOInterface tiersCollecteurDAO ;
	
	@Inject DocumentCtlInterface documentCtl ;
	
	@Inject EmailService emailService ;

	@Override
	public boolean valider(
			        ModeFinancement entity, 
			        boolean mustUpdateExistingNew,
			        String namedGraph,
			        boolean isFetchGraph,
			        Locale locale, 
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		
		//Verifier si la designation est non nulle
		
        if(entity.getDesignation() == null  ) {
        	
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
        	return false ;
        	
        }
        
        //Verifier l'entite programme immobilier
        
        if( entity.getProgrammeImmobilier() == null ){
        	
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
        	return false ;
        	
        }
        
        
        //Remettre le ProgrammeImmobilier dans le contexte de persistence 
        
        if(entity.getProgrammeImmobilier().getCode() ==  null) {
        	
        	if(entity.getProgrammeImmobilier().getId() == null) return false ;
        	
        	ProgrammeImmobilier programme =  EntityUtil
        			                           .findEntityById(entityManager,entity.getProgrammeImmobilier().getId(),
        			                                   namedGraph, isFetchGraph, ProgrammeImmobilier.class);
        	
        	
        	entity.getProgrammeImmobilier().setCode(programme.getCode());
        	
        }
        
   
        
		boolean isEntityDuplictedOrNotFound = new EntityUtil<ModeFinancement>().isEntityDuplicatedOrNotFound(
				entityManager, entity, mustUpdateExistingNew, "modeFinancementIdParDesignationParProgramme", 
				new String[] {"designation","programmeCode"}, 
				new String[] {entity.getDesignation(),entity.getProgrammeImmobilier().getCode() },
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
					
					
					EntityUtil<ModeFinancement> entityUtil = new EntityUtil<ModeFinancement>(ModeFinancement.class) ;
					
					
				    //ManyToOne ModeFinancement To ProgrammeImmobilier
					 //Verifie si le programme immobilier est non nul 
					 Map<String,String> msgVarMap = entity.getProgrammeImmobilier()== null 
													  || entity.getProgrammeImmobilier().getMsgVarMap() == null
												      ?  new HashMap<String,String>() 
												      : entity.getProgrammeImmobilier().getMsgVarMap() ;
					
					
					boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						           entity, entity.getProgrammeImmobilier(), 
						           entity.getClass().getMethod("setProgrammeImmobilier", ProgrammeImmobilier.class), null, true, 
							locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
					
					
					if (!isAttached) return false ;
					
					//OneToOneModeFinancement vers TypeFinancement
					 //Verifie si le Type Financement  est non nul
					
					msgVarMap = entity.getTypeFinancement()== null 
									|| entity.getTypeFinancement().getMsgVarMap() == null
								    ?  new HashMap<String,String>() : entity.getTypeFinancement().getMsgVarMap() ;
					
			        isAttached = entityUtil.attachLinkedEntity(entityManager, 
									entity, entity.getTypeFinancement(), 
									entity.getClass().getMethod("setTypeFinancement", TypeFinancement.class), null, true, 
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
				
			
				//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
				
				Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
				Set<ConstraintViolation<ModeFinancement>> constraintViolationList = validator.validate(entity) ;
				
				for ( ConstraintViolation<ModeFinancement> violation : constraintViolationList) {
					
					
					String translatedMessage = MessageTranslationUtil.translate(locale ,
							AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
							AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
							entity.getMsgVarMap()) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
					
					
				}
						
				if (!constraintViolationList.isEmpty()) return false ;
				
				// verification d'integrite complexes(règles metiers) 
				
				 if(entity.getPallierComptantSurSituationList() != null  
						 					&& !entity.getPallierComptantSurSituationList().isEmpty()) {
						 
						 if ( ! ( somme(entity.getPallierComptantSurSituationList()) == 100d ) ) {
						 
						 String msg  = MessageTranslationUtil.translate(locale,
							 		AppMessageKeys.CODE_TRADUCTION_SOMME_PALLIER,
							 		AppMessageKeys.CODE_TRADUCTION_SOMME_PALLIER, 
							 	entity.getMsgVarMap() ) ;
					  
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						 
						 
						 return false ;
					 
						}
						
				 }
				
		return true;
		
	}
	
	
	@Override
	public ModeFinancement validerEtEnregistrer(
			                ModeFinancement entity, 
			                boolean mustUpdateExistingNew,
			                String namedGraph, 
			                boolean isFetchGraph,
			                Locale locale, 
			                User loggedInUser,
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
			
			try{ ejbContext.setRollbackOnly();  } catch(Exception exx){} 
			
			return null ; 
		}
		
		
		/**
		 * 
		 * Proposition faite par Alzouma Moussa Mahamadaou , Date: 27/07/2021
		 * 
		 */
	
		//Appel à la classe correspondante si le financement est de type ComptantSurSituation

				//Verifie si getPallierComptantSurSituationList() non null
				
				if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null 
											        && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_COMPTANT_SUR_SITUATION)) {
					
					  
						if(entity.getPallierComptantSurSituationList() == null 
								        || entity.getPallierComptantSurSituationList().isEmpty() ) {
								  
								Map<String,String> msgVarMap =  new HashMap<String,String>();
								
								String msg  = MessageTranslationUtil.translate(locale,
										 AppMessageKeys.CODE_TRADUCTION_COMPTANT_SUR_SITUATION_NON_DEFINI,
										 AppMessageKeys.CODE_TRADUCTION_COMPTANT_SUR_SITUATION_NON_DEFINI, 
										 msgVarMap) ;
								msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							  
							   
								return null ;
							  
						  }
		
						
						 	//Appel à la classe PallierComptantSurSituation 
					
						 for(PallierComptantSurSituation entity1: entity.getPallierComptantSurSituationList()) {
						 
					
						 	PallierComptantSurSituation rtnPallier = pallierComptantSurSituationDAO
						 						.validerEtEnregistrer(entity1,mustUpdateExistingNew, 
						 						namedGraph, isFetchGraph, locale, loggedInUser, msgList);
					 
							if(rtnPallier == null) return null ;
									 
					 
						 }
						 
				
			}
		
		
		// Appel à la classe correspondante si le financement est de type CreditBancaire
		
		if(entity.getTypeFinancement().getId() != null 
				       && entity.getTypeFinancement().getId() != null
				       && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_CREDIT_BANCAIRE)) {
			
			 //Vérification de la creation d'un credit bancaire
			  
			  if(entity.getCreditBancaire() == null ) {
				  
					Map<String,String> msgVarMap =  new HashMap<String,String>();
					
					String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_CREDIT_BANCAIRE_NON_DEFINI,
							 AppMessageKeys.CODE_TRADUCTION_CREDIT_BANCAIRE_NON_DEFINI, 
							 msgVarMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				  
				     return null ;
				  
			  }
			  
			  
			   /**
				 * 
				 * Creation de la lettre de confort si elle existe
				 * 
				 */
			 
			
				 if(entity.getCreditBancaire().getLettreConfort() != null 
						   && entity.getCreditBancaire().getLettreConfort().getContenu() != null ) {
						
						boolean updateStringContent = false ;
						
						
						Document document = documentCtl
											 .creerModifierDocument(
											    entity.getCreditBancaire().getLettreConfort(), 
												mustUpdateExistingNew,updateStringContent, 
												namedGraph,isFetchGraph, locale, loggedInUser, msgList);
						
						if (document == null) {
							
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
							
							return null ;
							
						}
						
				}
				 
				 
				//Persistence du mode de financement de type credit banacire
				  
				CreditBancaire rtnCredit = creditBancaireDAO.validerEtEnregistrer(entity.getCreditBancaire(), 
						                                     mustUpdateExistingNew, namedGraph, isFetchGraph, 
						                                     locale, loggedInUser, msgList);
			 
			    if(rtnCredit == null) return null ;
				 
			
		}
		
		// Appel à la classe correspondante si le financement est de type temperament
		
		if(entity.getTypeFinancement() != null 
				      && entity.getTypeFinancement().getId() != null 
					  && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_TEMPERAMENT)) {
					
	
			//Verification du mode de financement de type Temperament
			
			 if(entity.getTemperament() == null ) {
				  
					Map<String,String> msgVarMap =  new HashMap<String,String>();
					
					String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_TEMPERAMENT_NON_DEFINI,
							 AppMessageKeys.CODE_TRADUCTION_TEMPERAMENT_NON_DEFINI, 
							 msgVarMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				  
				   
					return null ;
				  
			  }
			 
			//Appel à la classe DAO Temperament pour persistence
			 
			Temperament rtnTemperament = temperamentDAO
					                         .validerEtEnregistrer(
					                        	entity.getTemperament(), 
							                    mustUpdateExistingNew, 
							                    namedGraph, isFetchGraph, 
							                    locale, loggedInUser, msgList);
					 
			
				if(rtnTemperament == null)  return null ;
					
			}
		
		
		// Appel à la classe correspondante si le financement est de type tiers collecteur
		 
		if(entity.getTypeFinancement() != null 
				        && entity.getTypeFinancement().getId() != null 
					    && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_TIERS_COLLECTEUR)) {
							
			
			//Verification du mode de financement de type tiers collecteur
			
			 if(entity.getTierscollecteur() == null ) {
				  
					Map<String,String> msgVarMap =  new HashMap<String,String>();
					
					String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_TIERS_COLLECTEUR_NON_DEFINI,
							 AppMessageKeys.CODE_TRADUCTION_TIERS_COLLECTEUR_NON_DEFINI, 
							 msgVarMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				  
					return null ;
				  
			  }
			  
			   //Appel à la classe DAO TiersCollecteur pour persistence
			 
		       TiersCollecteur rtnTiers = tiersCollecteurDAO
		    		                         .validerEtEnregistrer(
		    		                               entity.getTierscollecteur(), 
									               mustUpdateExistingNew, 
									               namedGraph, isFetchGraph, 
									               locale, loggedInUser, msgList);
							 
				 if(rtnTiers == null) 	return null ;
									
							
				}
				
			
				ModeFinancement rtn = EntityUtil.persistOrMerge(
						entityManager, ModeFinancement.class, entity, 
						namedGraph, isFetchGraph, 
						AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
						locale, msgList);
				
				if (rtn == null) {
					
					try{ ejbContext.setRollbackOnly();; } catch(Exception exx){}
					
					return null ;
				}
		
		
	 return rtn;
	 
		
}
	
	
//Validation d'un mode de financement
	
 public ModeFinancement validerEtConfirmer(
				           String idModeFinancement,
				           boolean mustUpdateExistingNew,
				           String namedGraph,
				           boolean isFetchGraph, 
				           Locale locale, User loggedInUser,
				           List<NonLocalizedStatusMessage> msgList) {
		
	
		if (idModeFinancement == null) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
			
		}
		
		
		//Remettre l'entite dans le contexte de persistence
		
		ModeFinancement rtn = entityManager.find(ModeFinancement.class, idModeFinancement) ;
		
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
		
		
		//Verifier si le mode de financementr est actif
		
		if(rtn.getEstValide() != null && rtn.getEstValide()) {
		
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_VALIDE,
					 AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_VALIDE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
			
		}
	
		//Modification de la propriété estValide à true dans le contexte de persistence

		rtn.setEstValide(true);
		
		entityManager.flush();
		
		return  rtn ;
		
    }

	//Notification d'un mode de financement à un EFI
	@Override
	public boolean validerEtNotifier(
						ModeFinancement entity, 
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale, User loggedInUser,
						List<NonLocalizedStatusMessage> msgList) {
	
			//Verifier si l'entité recue est non nulle
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			
			if(entity.getCreditBancaire() == null || entity.getCreditBancaire().getEfi() == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						ModeFinancement.CODE_TRADUCTION_EFI_NON_DEFINIE,
						ModeFinancement.CODE_TRADUCTION_EFI_NON_DEFINIE, 
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				return false ;
				
			}
			
			
			//Appel à la methode de validation d'une entité
			
			boolean estValide = valider(entity, mustUpdateExistingNew, 
					              namedGraph, isFetchGraph,locale, msgList) ;
		
			
			if (!estValide) {
				
				try{ ejbContext.setRollbackOnly();  } catch(Exception exx){} 
				
				return false ; 
			}
			
			//Appel du service de notification
			
			//Recuperation de l'adresse mail
		
		   String email = entity.getCreditBancaire().getEfi().getEmail() != null 
					 			? entity.getCreditBancaire().getEfi().getEmail()
					 			: null ;
				 			
			
			//Verification de la variable mail
				 			
			if(email == null ||  email.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE,
						EFi.CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE, 
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				return false ;
				
			}
				  
			
		    //Initialisation de l'objet de mail
			
			String subject  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AUX_EFIS_PARTENAIRES, // venant du fichier
						 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AUX_EFIS_PARTENAIRES, // Message par defaut
					entity.getMsgVarMap()) ;
			
			
			//Vérifier si l'objet a été chargé
			
			if(subject == null || subject.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 entity.getMsgVarMap()) ;
				
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ;
				
			}
			
			//Addresses email des EFis partenaires
		   
		    
		    InternetAddress to  ;
		    
		    to = new InternetAddress() ;
	    	to.setAddress(email);
	    	
	    	InternetAddress[] toList = {to} ;
	
			//Adresse de copie carbone du  promoteur du programme immobilier 
			
			InternetAddress cc = new InternetAddress();
			
			cc.setAddress(entity.getProgrammeImmobilier() != null
					
							   &&  entity.getProgrammeImmobilier().getPromoteur() != null
							   &&  entity.getProgrammeImmobilier().getPromoteur().getEmail() != null
							   
							   ?   entity.getProgrammeImmobilier().getPromoteur().getEmail()
									   
							   :   "");
			
			
			InternetAddress[] ccList = {cc} ;
			
			String htmlContent = "" ;
			
			//Initialisation du contenu du message
		    
			String textContent  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AUX_EFIS_PARTENAIRES, // venant du fichier
					 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AUX_EFIS_PARTENAIRES, // Message par defaut
					entity.getMsgVarMap()) ;
			
			//Vérifier si le contenu de la notification a été bien chargé
			
			if(textContent == null || textContent.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					entity.getMsgVarMap()) ;
				
			    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
					
			    return false ;
			    
			  }
			
			//Try Catch
			
		    try {
		    	  
		    	 //Appel à la methode d'envoi de notification
		    	
		    	 emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
		    	   	
		     } catch (UnsupportedEncodingException | IllegalArgumentException
		    		          | MessagingException | EmailServiceException e) {
					
					logger.info("_673 Erreurs d'envoie de la notification aux Efis");
					
					return false ;
					
		     }
		        
		
		return  true ;
		
	}
	
	
	//Cette methode est créée pour créer chaque mode de financement dans une nouvelle transaction
	//De sorte que l'echec de création d'un mode de financement, ne puisse pas annuler
	//La creation des autres mode de financements , rattachés au programme immobilier
		
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ModeFinancement validerEtEnregistrerNewTransactional(
				ModeFinancement entity, boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale, User loggedInUser,
				List<NonLocalizedStatusMessage> msgList) {
		
			
				ModeFinancement	rtn = validerEtEnregistrer( entity,mustUpdateExistingNew, 
										namedGraph, isFetchGraph,  locale, loggedInUser, msgList) ;
		
				return rtn;
				
	}

//Methode somme
public double somme(Set<PallierComptantSurSituation> entity){
		
		Double rtn = 0D ;
		  
		for(PallierComptantSurSituation entity1: entity) {
			  
			if(entity1 == null || entity1.getTaux() == null) return rtn ;
			  
			 rtn = rtn + entity1.getTaux() ;
			  
		 }
			                      
		 return rtn;
		 
}


@SuppressWarnings("unchecked")
@Override
public List<ModeFinancement> rechercherModeFinancementListParProgrammeImmobilier(
		                      String idPrgrammeImmmobilier,
		                      boolean mustUpdateExistingNew, 
		                      String namedGraph, boolean isFetchGraph, 
		                      Locale locale, User loggedInUser,
		                      List<NonLocalizedStatusMessage> msgList) {
	
	
			if ( idPrgrammeImmmobilier == null  ) return null ;
			
			
			List<ModeFinancement>  rtnList = new ArrayList<ModeFinancement>();
			
			try {
				
				rtnList = entityManager
						     .createNamedQuery("rechercherModeFinancementListParPogrammeimmobilier")
						     .setParameter("idProgrammeImmobilier", idPrgrammeImmmobilier)
						     .getResultList() ;
						    
				
			}catch(NoResultException ex) {
				
				return null ;
				
			}
	          
		return rtnList;
	
}



@SuppressWarnings("unchecked")
@Override
public List<ModeFinancement> rechercherModeFinancementListParEfi(
		                      String idEfi, 
		                      boolean mustUpdateExistingNew,
		                      String namedGraph, boolean isFetchGraph, 
		                      Locale locale, User loggedInUser,
		                      List<NonLocalizedStatusMessage> msgList) {

		
		if ( idEfi == null  ) return null ;
		
		
		List<ModeFinancement>  rtnList = new ArrayList<ModeFinancement>();
		
		try {
			
			rtnList = entityManager
					     .createNamedQuery("rechercherModeFinancementListParEfi")
					     .setParameter("idEfi", idEfi)
					     .getResultList() ;
					    
			
		}catch(NoResultException ex) {
			
			return null ;
			
		}
		
		
		return rtnList;
		
}


	@Override
	public boolean supprimer(
			        String idModeFinancement, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
			
			// Verification de la variable
			
			if(idModeFinancement == null || idModeFinancement.isEmpty() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
		
			//Remettre l'entité dans le contexte de persistence
			
			ModeFinancement financement = EntityUtil
					                        .findEntityById(entityManager, idModeFinancement, "graph.modeFinancement.id" , isFetchGraph, ModeFinancement.class) ;
			
			
			//Verification de l'entité trouvée
			
			if (financement == null) {
				
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
				
			}
			
			//Suppression de l'entité principale
		
			entityManager.remove(financement);
			
			return true;
		
	}
	
	
}
