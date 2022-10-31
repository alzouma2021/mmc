package com.siliconwise.mmc.produitlogement;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.Ville;
import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBoolean;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDate;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDouble;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloat;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementInteger;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLong;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReference;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementString;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexte;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * Cette classe renferme les informations relatives à la classe d'acces Produit
 * Logement Ses mehodes sont implementées par l'interface ProduitLogementInt
 * 
 * @author Alzouma Moussa Mahamadou @ date 13/01/2021
 */
@Stateless
public class ProduitLogementDAO implements ProduitLogementDAOInterface {

		
		@Resource
		private EJBContext ejbContext;
	
		@PersistenceContext
		EntityManager entityManager;
		
		@Inject ProduitLogementDocumentCtlInterface produitLogementDocumentCtl ;
		
		@Inject DocumentCtlInterface documentCtl ;
				
		private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		// evenement d'historisation
		
		
		@Inject Event<HistoryEventPayload<ProduitLogement>> historyEvent ;
		
		// ==================== Recherche ======================================
		
		//Variable SQL_WHERE generale
		
		private String sqlWhere = ""; // clause SQL WHERE à construire
		private String sqlFrom = "" ; // clause SQL FROM à construire


		@SuppressWarnings({ "unchecked" })
		@Override
		public List<ProduitLogement> rechercherProduitLogementParCritereList(
				             List<CritereRechercheProduitLogement> critereList,
				             List<NonLocalizedStatusMessage> msgList) {
			
			 //Variables de la methode 
			
			 SessionBag currentSession =  new SessionBag() ;
			 Locale langue = SessionUtil.getLocale(currentSession) ;

			// verifier la liste des  arguments de la methode
			// c'est à dire verifier si la liste est vide ou non definie
			 
			if(critereList == null || critereList.isEmpty()) return null ; 
		
			//Construire le texte de la requete
				// Identifier le(s) typ(e)s des donnees retournees par la requete (clause select )
			
				final String SQL_SELECT = "SELECT pl ";
				
				// Identifier les elements principaux sur lesquels portent la requete  ( clause from )
				
				final String SQL_FROM = "ProduitLogement pl ";
				
				final String SQL_WHERE = " pl.estActive = true " ;
			
				//Identifier les differentes conditions et les elements complementaires qui permettent de retourner exactement les resultats attendus( clause where et from complementaire ) 
				//Les Produits Logements qui satisfont à chacun des criteres de la liste
				// Un produit logement satisfait à un critere s'il existe une caracteristique du produit logement dont :
				// 		-la propriete est egale à la propriete du critere
				//		-La valeur  satisfait la valeur du critere selon le comparateur dudit critere
				

			final String SQL_FROM_INTEGER = ",ValeurCaracteristiqueProduitLogementInteger vcplInteger ";
			final String SQL_FROM_BOOLEAN = ",ValeurCaracteristiqueProduitLogementBoolean vcplBoolean ";
			final String SQL_FROM_DATE = ",ValeurCaracteristiqueProduitLogementDate vcplDate ";
			final String SQL_FROM_DATETIME = ",ValeurCaracteristiqueProduitLogementDateTime vcplDateTime ";
			final String SQL_FROM_DOUBLE = ",ValeurCaracteristiqueProduitLogementDouble vcplDouble ";
			final String SQL_FROM_FLOAT = ",ValeurCaracteristiqueProduitLogementFloat vcplFloat ";
			final String SQL_FROM_LONG = ",ValeurCaracteristiqueProduitLogementLong vcplLong ";
			final String SQL_FROM_STRING = ",ValeurCaracteristiqueProduitLogementString vcplString ";
			final String SQL_FROM_TEXTE = ",ValeurCaracteristiqueProduitLogementTexte vcplTexte ";
			final String SQL_FROM_TIME = ",ValeurCaracteristiqueProduitLogementTime vcplTime ";
			final String SQL_FROM_REFERENCE = ",ValeurCaracteristiqueProduitLogementReference vcplReference ";
			final String SQL_FROM_VILLE = ",ValeurCaracteristiqueProduitLogementVille vcplVille ";
			
			
			final String SQL_WHERE_INTEGER =	"( vcplInteger member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplInteger.proprieteProduitLogement.type.id = :idProprieteCritereInteger "
					+  "AND vcplInteger.valeur :COMPARATEUR_CRITERE_INTEGER:  :valeurCritereInteger) " ;
			
			final String SQL_WHERE_BOOLEAN =   "( vcplBoolean member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplBoolean.proprieteProduitLogement.type.id = :idProprieteCritereBoolean "
					+  "AND vcplBoolean.valeur :COMPARATEUR_CRITERE_BOOLEAN:  :valeurCritereBoolean ) " ;
			
			final String SQL_WHERE_DATE =   "( vcplDate member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplDate.proprieteProduitLogement.type.id = :idProprieteCritereDate "
					+  "AND vcplDate.valeur :COMPARATEUR_CRITERE_DATE:  :valeurCritereDate1 AND  :valeurCritereDate2 ) " ;
			
			final String SQL_WHERE_DATE1 =   "( vcplDate member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplDate.proprieteProduitLogement.type.id = :idProprieteCritereDate "
					+  "AND vcplDate.valeur :COMPARATEUR_CRITERE_DATE:  :valeurCritereDate ) " ;
			
			final String SQL_WHERE_DATETIME =   "( vcplDateTime member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplDateTime.proprieteProduitLogement.type.id = :idProprieteCritereDateTime "
					+  "AND vcplDateTime.valeur :COMPARATEUR_CRITERE_DATETIME:  :valeurCritereDateTime1 AND :valeurCritereDateTime2 ) " ;
			
			final String SQL_WHERE_DATETIME1 =   "( vcplDateTime member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplDateTime.proprieteProduitLogement.type.id = :idProprieteCritereDateTime "
					+  "AND vcplDateTime.valeur :COMPARATEUR_CRITERE_DATETIME:  :valeurCritereDateTime ) " ;
			
			final String SQL_WHERE_DOUBLE =   "( vcplDouble member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplDouble.proprieteProduitLogement.type.id = :idProprieteCritereDouble "
					+  "AND vcplDouble.valeur :COMPARATEUR_CRITERE_DOUBLE:  :valeurCritereDouble1 AND  :valeurCritereDouble2 ) " ;
			
			final String SQL_WHERE_FLOAT =   "( vcplFloat member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplFloat.proprieteProduitLogement.type.id = :idProprieteCritereFloat "
					+  "AND vcplFloat.valeur :COMPARATEUR_CRITERE_FLOAT:   :valeurCritereFloat1  AND  :valeurCritereFloat2  ) " ;
			
			final String SQL_WHERE_LONG =   "( vcplLong member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplLong.proprieteProduitLogement.type.id = :idProprieteCritereLong "
					+  "AND vcplLong.valeur :COMPARATEUR_CRITERE_LONG:  :valeurCritereLong ) " ;
			
			final String SQL_WHERE_STRING =   "( vcplString member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplString.proprieteProduitLogement.type.id = :idProprieteCritereString "
					+  "AND vcplString.valeur :COMPARATEUR_CRITERE_STRING:  :valeurCritereString ) " ;
			
			final String SQL_WHERE_TEXTE =   "( vcplTexte member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplTexte.proprieteProduitLogement.type.id = :idProprieteCritereTexte "
					+  "AND vcplTexte.valeur :COMPARATEUR_CRITERE_TEXTE:  :valeurCritereTexte ) " ;
			
			final String SQL_WHERE_TIME =   "( vcplTime member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplTime.proprieteProduitLogement.type.id = :idProprieteCritereTime "
					+  "AND vcplTime.valeur :COMPARATEUR_CRITERE_TIME:  :valeurCritereTime ) " ;
			
			final String SQL_WHERE_REFERENCE =   "( vcplReference member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplReference.proprieteProduitLogement.type.id = :idProprieteCritereReference "
					+  "AND vcplReference.valeur.id :COMPARATEUR_CRITERE_REFERENCE: :valeurCritereReference ) " ;
			
			final String SQL_WHERE_VILLE =   "( vcplVille member of pl.caracteristiqueProduitLogementList "
					+  "AND vcplVille.proprieteProduitLogement.type.id = :idProprieteCritereVille "
					+  "AND vcplVille.valeur.id :COMPARATEUR_CRITERE_VILLE: :valeurCritereVille ) " ;
			
			//Parcourir l'ensemble de criteres en mettant à jour les clauses where et from complementaire
			
			//Intialisation de sqlFrom
			
			sqlFrom = SQL_FROM ;
			
			 if(!sqlWhere.isEmpty()) sqlWhere = "";
			 
			//Construire la clause where et from pour cela d'abord:
			 
			for(CritereRechercheProduitLogement critere: critereList )
			{
				
				 //selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_INTEGER))
				 { 
					 
						String texteTypePropriete = "Integer" ;
						
						// Completer la construction des clauses Sql From et Where pour les criteres de tye Entier
						
						this.construireWhereEtFrom(SQL_WHERE_INTEGER, SQL_FROM_INTEGER, texteTypePropriete, critere) ;
					 
				 }
				    
				 
				 //selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_BOOLEAN))
				 { 
					 
				        String texteTypePropriete = "Boolean" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Boolean
				        
				        this.construireWhereEtFrom(SQL_WHERE_BOOLEAN, SQL_FROM_BOOLEAN, texteTypePropriete, critere) ;							 					 
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATE))
				 { 
					 
					 	
				        String texteTypePropriete = "Date" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Date
				        
				        if (critere.getValeurCritere().getValeur() == null ) return null ;
				        
				        if( critere.getValeurCritere().getValeur().size() == 2) {
				        	
				        	 this.construireWhereEtFrom(SQL_WHERE_DATE, SQL_FROM_DATE, texteTypePropriete, critere) ;
				        	 
				        }else {
				        	
				        	this.construireWhereEtFrom(SQL_WHERE_DATE1, SQL_FROM_DATE, texteTypePropriete, critere) ;
				        }
				        							 					 
							 					 
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATETIME))
				 { 
					  
					 	 String texteTypePropriete = "DateTime" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type DateTime
					 	 
					 	if (critere.getValeurCritere().getValeur() == null ) return null ;
				        
				        if( critere.getValeurCritere().getValeur().size() == 2) {
				        	
				        	 this.construireWhereEtFrom(SQL_WHERE_DATETIME, SQL_FROM_DATETIME, texteTypePropriete, critere) ;
				        	 
				        }else {
				        	
				        	this.construireWhereEtFrom(SQL_WHERE_DATETIME1, SQL_FROM_DATETIME, texteTypePropriete, critere) ;
				        }
				        		
							 					 
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DOUBLE))
				 { 
					 
					 	String texteTypePropriete = "Double" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Double
					 	
				        this.construireWhereEtFrom(SQL_WHERE_DOUBLE, SQL_FROM_DOUBLE, texteTypePropriete, critere) ;	
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_FLOAT))
				 { 
					 
					 	String texteTypePropriete = "Float" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Float
					 	
				        this.construireWhereEtFrom(SQL_WHERE_FLOAT, SQL_FROM_FLOAT, texteTypePropriete, critere) ;
							 					 
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_LONG))
				 { 
					 
					 	String texteTypePropriete = "Long" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Long
					 	
				        this.construireWhereEtFrom(SQL_WHERE_LONG, SQL_FROM_LONG, texteTypePropriete, critere) ;
							 					 
				 }
				 
				//selectionner lSQL_WHERE_TYPEa clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_STRING))
				 { 
					 
						    
					 	String texteTypePropriete = "String" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type String
					 	
				        this.construireWhereEtFrom(SQL_WHERE_STRING, SQL_FROM_STRING, texteTypePropriete, critere) ;
				 }
				 
				 //selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TEXTE))
				 { 
					 
					 	String texteTypePropriete = "Texte" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Texte
					 	
				        this.construireWhereEtFrom(SQL_WHERE_TEXTE, SQL_FROM_TEXTE, texteTypePropriete, critere) ;
				 }
				 
				//selectionner la clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TIME))
				 { 
					 
					 	String texteTypePropriete = "Time" ;
					 	
				        // Completer la construction des clauses Sql From et Where pour les criteres de type Time
					 	
				        this.construireWhereEtFrom(SQL_WHERE_TIME, SQL_FROM_TIME, texteTypePropriete, critere) ;
							 					 
				 }
				 
				//selectionner lSQL_WHERE_TYPEa clause where et la clause from  adequates en fonction du type de la propriete du critere courant
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_REFERENCE))
				 { 
					
					 /*
						    
					 	String texteTypePropriete = "Reference" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type String
				        this.construireWhereEtFrom(SQL_WHERE_REFERENCE, SQL_FROM_REFERENCE, texteTypePropriete, critere) ;
				        */
					 
					 	String texteTypePropriete = "Reference" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type String
					 	
				        this.construireWhereEtFrom(SQL_WHERE_REFERENCE, SQL_FROM_REFERENCE, texteTypePropriete, critere) ;
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_VILLE))
				 { 
					 
					 	String texteTypePropriete = "Ville" ;
				        
				        // Completer la construction des clauses Sql From et Where pour les criteres de type String
					 	
				        this.construireWhereEtFrom(SQL_WHERE_VILLE, SQL_FROM_VILLE, texteTypePropriete, critere) ;
				        
				 }
				
				
			}
			
			//Construction de la requete proprement dite
				
				//Concater les differentes parties de la requete ensemble en suivant l'ordre ci-dessous :
					//La partie SELECT à la partie FROM
					//La partie FROM à la partie WHERE
			
				    String sql = SQL_SELECT + " FROM " + sqlFrom + " "  
							+ (sqlWhere.equals("") ? "" : " WHERE " + sqlWhere
							+ " AND "+ SQL_WHERE );
				    //sql = '"'+sql+'"';
				    
				    
			logger.info("_385 Requete JPQL="+sql);
			
			//excuter la requete
			//Fait appel à une instance de EntityManager
				
				//utiliser la methode createQuery pour excuter la requete
				//Affecter le resultat de ladite excution de la requete dans une variable de type Query
			
				Query query = entityManager.createQuery(sql) ;
				
				
			// Initialisation des variables nommées de la requete 
				
				for(CritereRechercheProduitLogement critere: critereList )
				{
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_INTEGER))
						 { 		
								    
							      
							        if (critere.getValeurCritere().getValeur() == null ) return null ;
							        
							        //Verification du type de la variable de la valeur du critére
							        
									try{
										
										for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
											if(!(critere.getValeurCritere().getValeur().get(i) instanceof Integer ) ) return null ;
									}
									catch(IllegalArgumentException ex) {
										  
										  String msg  = MessageTranslationUtil.translate(langue,
												 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
												 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
												 	critere.getMsgVarMap() ) ;
										  
										  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
										  
										  return null ;
										 
									} 
									
									query.setParameter("valeurCritereInteger", critere.getValeurCritere().getValeur());
									query.setParameter("idProprieteCritereInteger", critere.getProprieteProduitLogement().getType().getId());
									
						 }
						
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_BOOLEAN))
						 { 
							
							 		if (critere.getValeurCritere().getValeur() == null ) return null ;
							 		
									try{
										
										for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
											if(!(critere.getValeurCritere().getValeur().get(i) instanceof Boolean ) ) return null ;
									}
									catch(IllegalArgumentException ex) {
									
										  String msg  = MessageTranslationUtil.translate(langue,
												 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
												 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
												 	critere.getMsgVarMap()) ;
										  
										  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
										  
										  return null ;
								
									} 
									
									query.setParameter("valeurCritereBoolean", critere.getValeurCritere().getValeur());
									query.setParameter("idProprieteCritereBoolean", critere.getProprieteProduitLogement().getType().getId());
						 }
						

						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATE))
						 { 
							
							 	if (critere.getValeurCritere().getValeur() == null ) return null ;
							 	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof LocalDate ) ) return null ;
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
									 
								} 
								
								
								if(critere.getValeurCritere().getValeur().size() == 1) {
									
									query.setParameter("valeurCritereDate", critere.getValeurCritere().getValeur().get(0));
									
								}else {
									
									query.setParameter("valeurCritereDate1", critere.getValeurCritere().getValeur().get(0));
									query.setParameter("valeurCritereDate2", critere.getValeurCritere().getValeur().get(1));
									
								}
								
								query.setParameter("idProprieteCritereDate", critere.getProprieteProduitLogement().getType().getId());
								
						 }
						
			
						 if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATETIME))
						 { 
							 
							 	if (critere.getValeurCritere().getValeur() == null ) return null ;
							 	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof LocalDateTime ) ) return null ;
				
								}
								catch(IllegalArgumentException ex) {
								
								  
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									 
									  return null ;
								} 
								
								if(critere.getValeurCritere().getValeur().size() == 2) {
				
									query.setParameter("valeurCritereDateTime1", critere.getValeurCritere().getValeur().get(0));
									query.setParameter("valeurCritereDateTime2", critere.getValeurCritere().getValeur().get(1));
									
								}else {
									
									query.setParameter("valeurCritereDateTime", critere.getValeurCritere().getValeur().get(0));
								
								}
								
								query.setParameter("idProprieteCritereDateTime", critere.getProprieteProduitLogement().getType().getId());
								
						 }
						
						 
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DOUBLE))
						 { 		
								
								
							 	if (critere.getValeurCritere().getValeur() == null ) return null ;
							 	
								try{
				
									 for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof Double ) ) return null ;
										 	
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));
									  
									  return null ;
								 
								} 
								
								query.setParameter("valeurCritereDouble1", critere.getValeurCritere().getValeur().get(0));
								query.setParameter("valeurCritereDouble2", critere.getValeurCritere().getValeur().get(1));
								
								query.setParameter("idProprieteCritereDouble", critere.getProprieteProduitLogement().getType().getId());
								
						 }
						
						 
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_FLOAT))
						 { 
							
							 	if (critere.getValeurCritere().getValeur() == null  ) return null ;
							 	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof Float ) ) return null ;
								}
								catch(IllegalArgumentException ex) {
								
								  
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								} 
									
								query.setParameter("valeurCritereFloat1", critere.getValeurCritere().getValeur().get(0));
								query.setParameter("valeurCritereFloat2", critere.getValeurCritere().getValeur().get(1));
								
								query.setParameter("idProprieteCritereFloat", critere.getProprieteProduitLogement().getType().getId());
						 }
						
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_LONG))
						 { 
							
								if (critere.getValeurCritere().getValeur() == null ) return null ;
								
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof Long ) ) return null ;
										 	
								}
								catch(IllegalArgumentException ex) {
								
								  
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									 
									  return null ;
								 
								} 
								
							    query.setParameter("valeurCritereLong", critere.getValeurCritere().getValeur());
								query.setParameter("idProprieteCritereLong", critere.getProprieteProduitLogement().getType().getId());
						 }
						
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_STRING))
						 { 	
								
							  	if (critere.getValeurCritere().getValeur() == null ) return null ;
							  	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof String ) ) return null ;	
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								 
								} 
								
								query.setParameter("valeurCritereString", critere.getValeurCritere().getValeur());
								query.setParameter("idProprieteCritereString", critere.getProprieteProduitLogement().getType().getId());
						 }
						 
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TEXTE))
						 { 
							
							 	if (critere.getValeurCritere().getValeur() == null ) return null ;
							 	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof String ) ) return null ;	
								}
								catch(IllegalArgumentException ex) {
								
								  
									  String msg  = MessageTranslationUtil.translate(langue,
												AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								} 
								
								query.setParameter("valeurCritereTexte", critere.getValeurCritere().getValeur());
								query.setParameter("idProprieteCritereTexte", critere.getProprieteProduitLogement().getType().getId());
						 }
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TIME))
						 { 		
							
								
								if (critere.getValeurCritere().getValeur() == null ) return null ;
								
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof LocalTime ) ) return null ;	
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								} 
								
								query.setParameter("valeurCritereTime", critere.getValeurCritere().getValeur());
								query.setParameter("idProprieteCritereTime", critere.getProprieteProduitLogement().getType().getId());
						 }
						
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_REFERENCE))
						 { 	
								
								//Modification du try catch
			
							  	if (critere.getValeurCritere().getValeur() == null ) return null ;
							  	
								try{
									
									for(int i = 0 ; i < critere.getValeurCritere().getValeur().size() ; i++) 
										if(!(critere.getValeurCritere().getValeur().get(i) instanceof String ) ) return null ;	
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								 
								}   
								
								query.setParameter("valeurCritereReference", critere.getValeurCritere().getValeur());
								query.setParameter("idProprieteCritereReference", critere.getProprieteProduitLogement().getType().getId());
								
								
						 }
						
						if (critere.getProprieteProduitLogement() != null 
								 &&  critere.getProprieteProduitLogement().getType() != null
								 &&	 critere.getProprieteProduitLogement().getType().getId() != null
								 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_VILLE))
						 { 	
								
							  	if (critere.getValeurCritere().getValeur() == null ) return null ;
							  	
							  	//Recuperation de(s) Id(s) des villes transmises
							  	
							  	List<Ville> valueList = new ArrayList<Ville>();
							  	
							  	List<String> valueListString = new ArrayList<String>();
							  	
							  	for(Object ville: critere.getValeurCritere().getValeur()) 
							  		valueList.add((Ville) ville) ; 
							  	
							  	for(Ville ville: valueList) 
							  		valueListString.add(ville.getId()) ; 
							  	
							  	//Try Catch
							  		
								try{
									
									for(String valeur: valueListString) 
										if(!(valeur instanceof String ) ) return null ;	
								}
								catch(IllegalArgumentException ex) {
								
									  String msg  = MessageTranslationUtil.translate(langue,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
											 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
											 	critere.getMsgVarMap()) ;
									  
									  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									  
									  return null ;
								 
								}                    
									
									query.setParameter("valeurCritereVille", valueListString);
									query.setParameter("idProprieteCritereVille", critere.getProprieteProduitLogement().getType().getId());
									
								
						 }
				}
				
				
			//Retourner le resultat de la requete
				
				//Reverifier le type de données retournées par la quete dans la signature de la methode
					// ( Type Données Retournées : Liste Produits Logements)
				
					//Créer une variable de type de données retournées par la requete
					//List<ProduitLogement> rtnList = null ;
					
					//Affecter le resultat de la requete dans ladite variable créee 
				
					List<ProduitLogement> rtnList = (List<ProduitLogement>) query.getResultList();
					
					//Retourner la variable
					
					return rtnList ;
					
		}
		
		
		/** Completer la construction dees clauses Where et From à partir du critere courant 
		 * en fonction du type de la propriété du dit critère
		 * @param sqlWhereType clause sql WHERE correspondant au type de la propriété du critère
		 * @param sqlFromType clause sql FROM complémentaire correspondant au type de la propriété du critère
		 * @sqlWhere clause sql where existante
		 * @sqlFrom clause sql from existante
		 * @param texteTypePropriete texte indiquent le type de la propriété du critère courant
		 */
		public void construireWhereEtFrom(
				       String sqlWhereType, 
				       String sqlFromType, 
				       String texteTypePropriete,
					   CritereRechercheProduitLogement critere ) {


			String whereCritere = sqlWhereType
					.replaceAll(":COMPARATEUR_CRITERE_" + texteTypePropriete.toUpperCase() + ":",
					critere.getOperateurCritere().getCode());
						
			// Ajouter ladite clause where à la clause where globale existante en faisant un
			this.sqlWhere += (this.sqlWhere.equals("") ? "" : " AND ") + whereCritere;
			
			// Construction de la clause from specifique
			// Verifier si la clause from selectionnée n'existe pas dans la clause from
			// générale
			// Ajouter ladite clause from selectionnée si elle n'y est pas déjà à la clause
			// from generale
			
			if (!this.sqlFrom.contains("vcpl" + texteTypePropriete)) this.sqlFrom += " " + sqlFromType ;
		}

		//============== Persistence =======================================//
			
		
		public boolean  valider(ProduitLogement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph,
					Locale locale,
					List<NonLocalizedStatusMessage> msgList) {
			
		
			//Verifier si le code du produit logement est non nul
			
			if(entity.getCode() == null || entity.getProgrammeImmobilier() == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_PRODUITLOGEMENT_CODE_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_PRODUITLOGEMENT_CODE_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			 
			// recherche de doublon
			
			boolean isEntityDuplictedOrNotFound = new EntityUtil<ProduitLogement>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "produitLogementIdParCodeParProgrammeImmobilier", 
					new String[] {"code","programmeimmobilier"}, new String[] {entity.getCode(),entity.getProgrammeImmobilier().getCode()},
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
				
				EntityUtil<ProduitLogement> entityUtil = new EntityUtil<ProduitLogement>(ProduitLogement.class) ;
				
				
			    //ManyToOne ProduitLogement vers ProgrammeImmobilier
				 //Verifie si le programmeimmobilier est non nul 
				
				 Map<String,String> msgVarMap = entity.getProgrammeImmobilier() == null 
													|| entity.getProgrammeImmobilier().getMsgVarMap() == null
											   ?  new HashMap<String,String>() : entity.getProgrammeImmobilier().getMsgVarMap() ;
				
				
				boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						entity, entity.getProgrammeImmobilier(), 
						entity.getClass().getDeclaredMethod("setProgrammeImmobilier", ProgrammeImmobilier.class), null, true, 
						locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
				
				
				if (!isAttached) return false ;
				
			
			} 
			catch(Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				logger.error("_960 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
				
				ex.printStackTrace();
				
				return false ;
			}
			
			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<ProduitLogement>> constraintViolationList = validator.validate(entity) ;
			
			for (ConstraintViolation<ProduitLogement> violation : constraintViolationList) {
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
		
			
			return true;
			
		}


		public ProduitLogement validerEtEnregistrer(
								ProduitLogement entity,
								boolean mustUpdateExistingNew,
								String namedGraph, boolean isFetchGraph, 
								Locale locale,  User loggedInUser, 
								List<NonLocalizedStatusMessage> msgList){
						
			
			//Verifier l'entité transmise
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
			}
			
			// Appel de la methode Valider
			
			boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
									locale, msgList) ;
			
			
			if (!estValide) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return null ; 
			}
			
			
			// est une creation ?
			
			boolean estCreation = entity.getId() == null ;
		
			
			//Methode de persistence de l'entité correspondante
			
			ProduitLogement rtn = EntityUtil.persistOrMerge(
					entityManager, ProduitLogement.class, entity, 
					namedGraph, isFetchGraph, 
					AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
					locale, msgList);
			
			if (rtn == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return null ;
			}
			
			//Historisation de la creation ou de la modification d'un produit logement
			
			String loggedInUserId = loggedInUser.getId() != null 
				                    ? loggedInUser.getId() 
				                    : null ;
				                    
		    String  observation  = null ;

            String loggedInUserFullname = loggedInUser.getFullname() != null 
				                    ? loggedInUser.getFullname() 
				                    : null ;

			HistoryEventType historyEventType = estCreation 
						? HistoryEventType.CREATION : HistoryEventType.MODIFICATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
								loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			//Retourne le resultat
			
			return rtn ;
			
		}

		
		@Override
		public boolean validerEtConfirmer(
						 String idProduitLogement, 
						 boolean mustUpdateExistingNew,
						 String namedGraph, boolean isFetchGraph, 
						 Locale locale, User loggedInUser,
						 List<NonLocalizedStatusMessage> msgList) {
			
			
			//Verification de la variable idProduitLogement
			
			if(idProduitLogement == null ) return false ;
			
			
			//Remettre l'entité produit logement dans le contexte de persistence
			
			ProduitLogement entity = entityManager.find(ProduitLogement.class, idProduitLogement) ;
			
			//Verification de l'entité
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
			}
			
			//Verificationde la liste de caracteristiques
			
			if (entity.getCaracteristiqueProduitLogementList() == null ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			
			//verifier si toutes les caracteristiques obligatoires ont été renseignées.
			
			Set<CaracteristiqueProduitLogement> entityList = entity.getCaracteristiqueProduitLogementList() ;
			
			boolean aToutesSesCaracteristiquesObligatoiresRenseignees = verifierLesCaracteristiquesObligatoires(entityList) ;
			
			
			if (!aToutesSesCaracteristiquesObligatoiresRenseignees) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return false ; 
			}
	
			
			//Modification de la propriété estValide dans le contexte de persistence
			
			entity.setEstValide(true) ;
			
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
				                    ? loggedInUser.getId() 
				                    : null ;

            String loggedInUserFullname = loggedInUser.getFullname() != null 
				                    ? loggedInUser.getFullname() 
				                    : null ;
				                    
	        String   observation  = null ;

			HistoryEventType historyEventType = HistoryEventType.VALIDATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							loggedInUserId, loggedInUserFullname, observation, locale) ;
	
			
			//Retourne le resultat
			
			return  true ;
			
			
		}

		@Override
		public boolean validerEtActiver(
						 String idProduitLogement, 
						 boolean mustUpdateExistingNew,
						 String namedGraph, boolean isFetchGraph,
						 Locale locale, User loggedInUser,
						 List<NonLocalizedStatusMessage> msgList) {
			
			
			
			//Verification de la variable idProduitLogement
			
			if(idProduitLogement == null || idProduitLogement.isEmpty()) return false ;
			
			
			//Remettre l'entité produit logement dans le contexte de persistence
			
			ProduitLogement entity = entityManager.find(ProduitLogement.class, idProduitLogement) ;
			
			//Verification de l'entité
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
			}
			
			
			//Verification d'integrite complexes(règles metiers) le cas echeant
		
		  	//Tout produit logement actif , doit être validé. 
			
			if(entity.getEstValide() == null || !entity.getEstValide() ) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_NON_VALIDE_PAR_PROMOTEUR,// venant du fichier
						ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_NON_VALIDE_PAR_PROMOTEUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
		    	
		    }
			
			//Verifier pour tout produit logement activé
		
			//Tout produit logement actif doit avoir son programme immobilier  validé par le promoteur
			
		    //Appel à la requete pour verifier si le programmeImmobilier est validé
				
			Long result  =  (Long) entityManager
							 .createNamedQuery("programmeImmobilierValide")
							 .setParameter("idProgrammeImmobilier", entity.getProgrammeImmobilier().getId())
							 .getSingleResult() ;
				
				
			logger.info("_1265 ProgrammeImmbilier validé="+result);
				
			if( result != 1) {
					
			  String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_PROGRAMME_IMMOBILIER_NON_PAR_PROMOTEUR,// venant du fichier
					ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_PROGRAMME_IMMOBILIER_NON_PAR_PROMOTEUR, // Message par defaut
					entity.getMsgVarMap()) ;
			  msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
					
			  return false ;
					
			}
				
			//Modification de la propriété estAcive dans le contexte de persistence
				
			entity.setEstActive(true);
				
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
			                    ? loggedInUser.getId() 
			                    : null ;

			String loggedInUserFullname = loggedInUser.getFullname() != null 
			                    ? loggedInUser.getFullname() 
			                    : null ;
			                    
		    String   observation  = null ;
			
			HistoryEventType historyEventType = HistoryEventType.ACTIVATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							loggedInUserId, loggedInUserFullname, observation, locale) ;
		
			
			//Retourne le produit logement activé
				
			return true ;
				
		}

		
		@Override
		public boolean validerEtDesactiver(
						 String idProduitLogement,
						 boolean mustUpdateExistingNew,
						 String namedGraph, boolean isFetchGraph, 
						 Locale locale, User loggedInUser,
						 List<NonLocalizedStatusMessage> msgList) {
			
			
			///Verification de la variable idProduitLogement
			
			if(idProduitLogement == null ) return false ;
			
			
			//Remettre l'entité produit logement dans le contexte de persistence
			
			ProduitLogement entity = entityManager.find(ProduitLogement.class, idProduitLogement) ;
			
			//Verification de l'entité
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
			}
			
			//Verifier si le produit logement est actif
			
			if(entity.getEstActive() == null || !entity.getEstActive()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_NON_ACTIVE,// venant du fichier
						ProduitLogement.CODE_TRADUCTION_PRODUIT_LOGEMENT_NON_ACTIVE, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				return false ;
				
			}
			
			//Modification de la propriété estActive dans le contexte de persistence 
			
			entity.setEstActive(false) ;
			
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
			                    ? loggedInUser.getId() 
			                    : null ;

			String loggedInUserFullname = loggedInUser.getFullname() != null 
			                    ? loggedInUser.getFullname() 
			                    : null ;
			
		    String   observation  = null ;
			
			HistoryEventType historyEventType = HistoryEventType.DESACTIVATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
								loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			
			//Retourne le produit logement desactivé
			
			return   true ;
			
		
		}
		
		@Override
		public boolean supprimer(
					String idProduitLogement, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
			
			
			// Vérification de l'id du produit logement à supprimer 
			
			if(idProduitLogement == null || idProduitLogement.isEmpty() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return false ; 
				
			}
		
			//Remettre le produit logement  dans le contexte de persistence avec utilisation de namedGraph

			ProduitLogement entity = EntityUtil.findEntityById(entityManager, 
					   idProduitLogement, namedGraph,isFetchGraph, ProduitLogement.class) ;
			
			
			//Vérification du produit logement mis en contexte de persistence
			
			if (entity == null || entity.getId() == null) {
				
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
			   return false ;
			   
				
			}
			
			//Verification des contraintes fonctionnelles liées au produit logement à supprimer
			
			//Verifier si le produit logement n'est pas actif
			
			if(entity.getEstActive() != null && entity.getEstActive() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_PRODUITLOGEMENT_ACTIF, // venant du fichier
					ProduitLogement.CODE_TRADUCTION_PRODUITLOGEMENT_ACTIF, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			
			//Recherche et Suppression des entités de type d'association agregation 
			
			//Recherche et Suppression des associations composites(
		
			
			//Rechercher  d'eventuels documents attachés au produit logement
			  
			List<Document>	entityList 
			                  = produitLogementDocumentCtl
			                     .rechercherDocumentListParIdProduitLogement(
			                       idProduitLogement,  mustUpdateExistingNew, 
			                       namedGraph, isFetchGraph, locale, msgList) ;
				
			
		   	//Verification de la variable entityList
			
			
			if(entityList != null && entityList.size()> 0) {
				
			//Suppression des informations de la classe d'asscoiation ProduitLogementDocument
			//pour eviter certaines erreurs de JPA
				
			int   deleteClassProduitLogementDocument 
				                      =  entityManager
										  .createNamedQuery("suppressionInformationsProduitLogementDocument")
										  .setParameter("idProduitLogement", idProduitLogement)
										  .executeUpdate();
			
			    
			    //Verifcation de la suppression , si non effectue alors on rollback la transaction
				
			   	if(deleteClassProduitLogementDocument == 0) {
			   		
			   		try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			   		
			   		return false ;
			   		
			   	}
			   	
			   //Appel à la methode supprimerDocumentList pour supprimer les documents( Metadats + Contenu )
				
			   boolean deleteDocument = documentCtl
									     .supprimerDocumentList(
										   entityList, mustUpdateExistingNew,
										   namedGraph,isFetchGraph, locale,  
										   loggedInUser, msgList);
			 
			   //Si suppression echou alors on retourne false
			   
			   if(!deleteDocument)  return false ;
			   
		
			}
			
			
			//Suppression définitive de l'entité principale
			
			entityManager.remove(entity);
			
			
			//Appel à la methode pour historiser la suppression de l'entité pour une question d'audit
			
			String loggedInUserId = loggedInUser.getId() != null 
				                    ? loggedInUser.getId() 
				                    : null ;

	        String loggedInUserFullname = loggedInUser.getFullname() != null 
				                    ? loggedInUser.getFullname() 
				                    : null ;
				                    
	        String   observation  = entity.getDesignation() != null
	        						? entity.getDesignation() 
	        						: null;

			HistoryEventType historyEventType = HistoryEventType.SUPPRESSION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
								loggedInUserId, loggedInUserFullname,observation,locale) ;
					
			
			return true ;
			
		}
		
	    
		@SuppressWarnings("unchecked")
		@Override
	    public List<ProduitLogement> rechercherProduitLogementList() {
			    
	    		   return (List<ProduitLogement>) entityManager
							.createNamedQuery("trouverTousLesProduitsLogements")
							.getResultList();
	    		  
		}		
		
		
		@Override
		public ProduitLogement rechercherUnProduitLogementParId(String id,
				String namedGraph, boolean isFetchGraph, Class<ProduitLogement> entityClass) {
			
			return EntityUtil
						.findEntityById(entityManager, id, namedGraph, isFetchGraph, entityClass);

		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<ProduitLogement> rechercherProduitLogementParMotCle(String motCles) {
			
			//Verification de l'argument
			//Verifier si l'argument n'est pas null
			
				if (motCles == null) return null ;
				
				//Verifier si le type de l'argument correspondant au type String
				try {
					
				    if( !(motCles instanceof String) ) return null ;
					
				}catch(IllegalArgumentException ex) {
					
					return null ;
				}
				
			//Construction de la requête 
			//Utiliser une requéte nommée 
			//Execution de la requête 
			//Retourne le résultat de la requéte
				
			List<ProduitLogement>  rtnList = entityManager
					.createNamedQuery("trouverTousLesProduitsLogementsDontLesProprietesOntLeMotCle")
					.setParameter("motCle", motCles + "%")
					.getResultList();
			
			return rtnList ;
			
		}
		
		
		@SuppressWarnings({ "unchecked" })
		@Override
		public List<ProduitLogement> rechercherProduitLogementParPromoteur(
								String promoteurId, 
								String namedGraph,
								boolean isFetchGraph, 
								Class<ProduitLogement> entityClass) {
			
			
			
				//Verification de l'argument
				//Verifier si l'argument n'est pas null
			
				if (promoteurId == null) return null ;
				
				//Verifier si le type de l'argument correspondant au type String
				
				try {
					
				    if( !(promoteurId instanceof String) ) return null ;
					
				}catch(IllegalArgumentException ex) {
					
					return null ;
				}
				
			//Construction de la requête 
			//Utilisation  d'une requête nommée 
			//Execution de la requête 
			//Retourne le résultat de la requête
				
			return (List<ProduitLogement>) entityManager
						.createNamedQuery("rechercheProduitLogementsParPromoteur")
						.setParameter("promoteurId", promoteurId)
						.getResultList();
			
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public List<CaracteristiqueProduitLogement> 
						rechercherCaracteristiquesParProduitLogement(
						String IdProduit,
						String namedGraph, boolean isFetchGraph, 
						Class<CaracteristiqueProduitLogement> entityClass) {
		
			
			//Verification de l'argument
			//Verifier si l'argument n'est pas null
			
			if (IdProduit == null) return null ;
			
			//Verifier si le type de l'argument correspondant au type String
			try {
				
			    if( !(IdProduit instanceof String) ) return null ;
				
			}catch(IllegalArgumentException ex) {
				
				return null ;
			}
			
			//Construction de la requête 
			//Utilisation  d'une requête nommée 
			//Execution de la requête 
			//Retourne le résultat de la requête
				
			return (List<CaracteristiqueProduitLogement>)  entityManager
					 .createNamedQuery("caracteristiquesParProduitLogement")
					 .setParameter("idProduit", IdProduit)
					 .getResultList() ;
		
		
		}

		

		/**
		 * 
		 * Methodes de factorisation de code , ecrites par Alzouma Moussa Mahamadou 
		 * 
		 */
		@Override
		public boolean verifierLesCaracteristiquesObligatoires(
							Set<CaracteristiqueProduitLogement> entityList) {
			

			//Vérifier l'entité
			
			if( entityList == null) return false ;
			
			//Pour chaque caracteristique dont l'attribut estObligatoire de la propriété est true
			//Alors verifie si la valeur est non nulle 
			//si la valeur est nulle alors renvoie un message d'erreurs
			
			for(CaracteristiqueProduitLogement entity: entityList) {
				
				
				if(entity != null && entity.getProprieteProduitLogement() != null
								  && entity.getProprieteProduitLogement().getEstObligatoire() != null
								  && entity.getProprieteProduitLogement().getEstObligatoire() == true ) {
					
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementInteger) {
					
						
						ValeurCaracteristiqueProduitLogementInteger	entityInteger = (ValeurCaracteristiqueProduitLogementInteger) entity;
						
						if( entityInteger.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementBoolean) {
						
						ValeurCaracteristiqueProduitLogementBoolean	entityBoolean = (ValeurCaracteristiqueProduitLogementBoolean) entity;
						
						if( entityBoolean.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementLong) {
						
						ValeurCaracteristiqueProduitLogementLong	entityLong = (ValeurCaracteristiqueProduitLogementLong) entity;
						
						if( entityLong.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementDouble) {
						
						ValeurCaracteristiqueProduitLogementDouble	entityDouble = (ValeurCaracteristiqueProduitLogementDouble) entity;
						
						if( entityDouble.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementFloat) {
						
						ValeurCaracteristiqueProduitLogementFloat	entityFloat = (ValeurCaracteristiqueProduitLogementFloat) entity;
						
						if( entityFloat.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementDate) {
						
						ValeurCaracteristiqueProduitLogementDate	entityDate = (ValeurCaracteristiqueProduitLogementDate) entity;
						
						if( entityDate.getValeur() == null) return false ;
						
					}
					
				
					if(entity instanceof ValeurCaracteristiqueProduitLogementDateTime) {
						
						ValeurCaracteristiqueProduitLogementDateTime	entityDateTime = (ValeurCaracteristiqueProduitLogementDateTime) entity;
						
						if( entityDateTime.getValeur() == null) return false ;
						
					}
					
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementTime) {
						
						ValeurCaracteristiqueProduitLogementTime	entityTime = (ValeurCaracteristiqueProduitLogementTime) entity;
						
						if( entityTime.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementDocument) {
						
						ValeurCaracteristiqueProduitLogementDocument	entityDocument = (ValeurCaracteristiqueProduitLogementDocument) entity;
						
						if( entityDocument.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementReference) {
						
						ValeurCaracteristiqueProduitLogementReference	entityReference = (ValeurCaracteristiqueProduitLogementReference) entity;
						
						if( entityReference.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementString) {
						
						
						ValeurCaracteristiqueProduitLogementString	entityString = (ValeurCaracteristiqueProduitLogementString) entity;
						
						if( entityString.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementTexte) {
						
						ValeurCaracteristiqueProduitLogementTexte	entityTexte = (ValeurCaracteristiqueProduitLogementTexte) entity;
						
						if( entityTexte.getValeur() == null) return false ;
						
					}
					
					if(entity instanceof ValeurCaracteristiqueProduitLogementVille) {
						
						ValeurCaracteristiqueProduitLogementVille	entityVille = (ValeurCaracteristiqueProduitLogementVille) entity;
						
						if( entityVille.getValeur() == null) return false ;
						
					}
					
					
				}
				
				
			}
			
			return true;
			
		}
		
		
}
