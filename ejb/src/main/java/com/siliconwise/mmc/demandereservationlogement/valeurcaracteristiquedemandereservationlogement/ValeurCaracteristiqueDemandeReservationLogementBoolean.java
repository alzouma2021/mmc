package com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;


/**
 * Cette classe renferme la valeur propriete produit logement boolean
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
@Entity
@Table(name="valeurcaracteristiquedemandereservationlogementboolean")
@DiscriminatorValue("boolean")
public class ValeurCaracteristiqueDemandeReservationLogementBoolean 
													extends ValeurCaracteristiqueDemandeReservationLogement 
													implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private Boolean valeur ;
   
	public ValeurCaracteristiqueDemandeReservationLogementBoolean() {
		super();
	}

	public Boolean getValeur() {
		return valeur;
	}
	
	public void setValeur(Boolean valeur) {
		this.valeur = valeur;
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	

public static ValeurCaracteristiqueDemandeReservationLogementBoolean from(ValeurCaracteristiqueDemandeReservationLogement valeur) {
		
	
		//Variables pour gerer les messages d'erreurs
		SessionBag currentSession =  new SessionBag() ;
		Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();

		
		//Instance de la classe que la methode va retourner
		ValeurCaracteristiqueDemandeReservationLogementBoolean rtn = new ValeurCaracteristiqueDemandeReservationLogementBoolean();
		
		//Recuperation de la valeur de la caracteristiqueProduitLogement
		 String valeurTexte = valeur.getValeurTexte() ;
		
		//Verification si la valeur est non nulle
		if 	( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
		
		//Conversion de la valeur String en Boolean
		Boolean valueBoolean = Boolean.parseBoolean(valeurTexte);
		
			//Si le type ne correspond à Boolean alors on retourne un null
				
				try {
						
						if(!(valueBoolean instanceof  Boolean ) ) return null ;
						
					}
					catch(IllegalArgumentException ex) {
					
						 String msg  = MessageTranslationUtil.translate(langue,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
									new String[] {}) ;
						 
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					     
					     return null ;
					} 
				
			
			rtn.setValeur(valueBoolean);
			
			rtn.setId(valeur.getId());
			
			rtn.setCaracteristiqueDemandeReservationLogement(valeur.getCaracteristiqueDemandeReservationLogement());
			
			//rtn.setDesignation(valeur.getDesignation());
			
			rtn.setDesignation(valeur.getDesignation());
			
			
			rtn.setDescription(valeur.getDescription());
			
			rtn.setVersion(valeur.getVersion());
	
			return rtn ;
			
	}


public static ValeurCaracteristiqueDemandeReservationLogement to(ValeurCaracteristiqueDemandeReservationLogementBoolean entity) {
	
	
	
	//Variables pour gerer les messages d'erreurs
	SessionBag currentSession =  new SessionBag() ;
	Locale langue = SessionUtil.getLocale(currentSession) ;
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
	
	
	//Instance que la methode retourne
	
	ValeurCaracteristiqueDemandeReservationLogement rtn = new ValeurCaracteristiqueDemandeReservationLogement() ;
	 
	if(entity.getValeur() == null) return null ;
	
	//Conversion de la valeur en texte
	
	 String valeur = String.valueOf(entity.getValeur()) ;
	
	
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
	  rtn.setCaracteristiqueDemandeReservationLogement(entity.getCaracteristiqueDemandeReservationLogement());;
	  rtn.setValeurTexte(valeur);

	  return rtn;
	
  }
	
}
