package com.siliconwise.mmc.produitlogement.caracteristique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.siliconwise.common.entity.IEntityMsgVarMap;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.critere.Valeur;
import com.siliconwise.mmc.produitlogement.critere.ValeurReference;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@NamedQueries(value={
		@NamedQuery(
				name="valeurCaracteristiqueProduitLogementReferenceParDesignation",
				query="SELECT vcplReference.id FROM ValeurCaracteristiqueProduitLogementReference vcplReference  "
					+ "WHERE vcplReference.designation = :designation")
})
@Entity
@Table(name="valeurcaracteristiqueproduitlogementreference")
@DiscriminatorValue("reference")
public class ValeurCaracteristiqueProduitLogementReference 
													extends CaracteristiqueProduitLogement 
														implements Serializable,IEntityMsgVarMap{

		private static final long serialVersionUID = 1L;
		
		@NotNull 
		@ManyToOne
		private Reference valeur  ;

		public ValeurCaracteristiqueProduitLogementReference() {
			super();
		}

		public Reference getValeur() {
			return valeur;
		}

		public void setValeur(Reference valeur) {
			this.valeur = valeur;
		}
		
		@Override
		public Map<String, String> getMsgVarMap() {
			
			Map<String,String> rtn =  new HashMap<String,String>();
			
			rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
			rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
			
			return   rtn;
		}
		

		@SuppressWarnings({ "unused", "unchecked" })
		public static ValeurCaracteristiqueProduitLogementReference from(CaracteristiqueProduitLogement valeur) {
		
				//Variables pour gerer les messages d'erreurs 
			    SessionBag currentSession =  new SessionBag() ;
			    Locale langue = SessionUtil.getLocale(currentSession) ;
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
				
				//Instance de la classe ValeurReference que la methode va retourner
				ValeurCaracteristiqueProduitLogementReference rtn = new ValeurCaracteristiqueProduitLogementReference();
				
				//Recuperation de la valeur du critere
				String valeurTexte = valeur.getValeurTexte();
				
				//Recuperation et verification de la valeur de la caracteristique
				//Reference  valeurReference = valeur.getValeurReference() ;
				
				//Verification de la variable valeurAconvertir
				if ( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
				
					try {
						
							if(!(valeurTexte instanceof String ) ) return null ;
							
						}
						catch(Exception ex) {
							
							String msg  = MessageTranslationUtil.translate(langue,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
									new String[] {}) ;
							
							msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							
						}
					
					rtn.setId(valeur.getId());
					
					rtn.setProprieteProduitLogement(valeur.getProprieteProduitLogement());
					
					//rtn.setDesignation(valeur.getDesignation());
					
					rtn.setDesignation( (rtn.getDesignation() == null || rtn.getDesignation() == "" ) 
							? valeur.getProprieteProduitLogement().getDesignation()
							: valeur.getDesignation());
	
					rtn.setDescription(valeur.getDescription());
					
					rtn.setVersion(valeur.getVersion());
					
					Reference reference = new Reference();
					
					reference.setId(valeurTexte);
					
					rtn.setValeur(reference);
			
					return rtn ;
					
					
			}
		
		
	public static CaracteristiqueProduitLogement  to(ValeurCaracteristiqueProduitLogementReference entity) {
		 
		//Variables pour gerer les messages d'erreurs
		SessionBag currentSession =  new SessionBag() ;
		Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		
		//Instance que la methode retourne
		
		CaracteristiqueProduitLogement rtn = new CaracteristiqueProduitLogement() ;
		 
		if(entity.getValeur() == null) return null ;
		
		//Recuperation de la valeur
		
		 String valeur = entity.getValeur().getId() ;
		
		
			try {
				
				if(!(valeur instanceof  String ) ) return null ;
				
			}
			catch(IllegalArgumentException ex) {
			
				 String msg  = MessageTranslationUtil.translate(langue,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
							new String[] {}) ;
				 
			     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			     
			     return null ;
			} 

		  rtn.setId(entity.getId());
		  rtn.setDescription(entity.getDescription());
		  rtn.setDesignation(entity.getDesignation());
		  rtn.setVersion(entity.getVersion());
		  rtn.setProprieteProduitLogement(entity.getProprieteProduitLogement());
		  rtn.setValeurTexte(valeur);

		  return rtn;
		
	}
	
}
