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

import com.siliconwise.common.Ville;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@NamedQueries(value={
		@NamedQuery(
				name="valeurCaracteristiqueProduitLogementVilleParDesignation",
				query="SELECT vcplVille.id FROM ValeurCaracteristiqueProduitLogementVille vcplVille  "
					+ "WHERE  vcplVille.designation = :designation")
})
@Entity
@Table(name="valeurcaracteristiqueproduitlogementville")
@DiscriminatorValue("ville")
public class ValeurCaracteristiqueProduitLogementVille 
													extends CaracteristiqueProduitLogement 
														implements Serializable,IEntityMsgVarMap{

		private static final long serialVersionUID = 1L;
		
		@NotNull 
		@ManyToOne
		private Ville valeur  ;

		public ValeurCaracteristiqueProduitLogementVille() {
			super();
		}

		public Ville getValeur() {
			return valeur;
		}

		public void setValeur(Ville valeur) {
			this.valeur = valeur;
		}

		@Override
		public Map<String, String> getMsgVarMap() {
			
			Map<String,String> rtn =  new HashMap<String,String>();
			
			rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
			rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
			
			return   rtn;
		}
		
	
		public static ValeurCaracteristiqueProduitLogementVille from(CaracteristiqueProduitLogement valeur) {
		
				//Variables pour gerer les messages d'erreurs 
			    SessionBag currentSession =  new SessionBag() ;
			    Locale langue = SessionUtil.getLocale(currentSession) ;
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
				
				//Instance de la classe ValeurVille que la methode va retourner
				ValeurCaracteristiqueProduitLogementVille rtn = new ValeurCaracteristiqueProduitLogementVille();
				
				//Recuperation de la valeur du critere
				String valeurTexte = valeur.getValeurTexte();
				
				//Recuperation et verification de la valeur de la caracteristique
				
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
	
					rtn.setVersion(valeur.getVersion());
					
					rtn.setDescription(valeur.getDescription());
					
					Ville ville = new Ville();
					
					ville.setId(valeurTexte);
					
					rtn.setValeur(ville);
			
					return rtn ;
					
					
			}
		
		
		public static CaracteristiqueProduitLogement  to(ValeurCaracteristiqueProduitLogementVille entity) {
			 
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
