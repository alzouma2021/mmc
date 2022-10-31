package com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 * @date  12/01/2021
 *
 */
@Entity
@Table(name="valeurcaracteristiquedemandereservationlogementdatetime")
@DiscriminatorValue("datetime")
public class ValeurCaracteristiqueDemandeReservationLogementDateTime extends ValeurCaracteristiqueDemandeReservationLogement implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = -546743011044064204L;

	
	@NotNull 
	@Column(nullable=false)
	private LocalDateTime valeur = null ;
	
	
	public ValeurCaracteristiqueDemandeReservationLogementDateTime() {
		super();
	}

	public LocalDateTime getValeur() {
		return valeur;
	}

	public void setValeur(LocalDateTime valeur) {
		this.valeur = valeur;
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ValeurCaracteristiqueDemandeReservationLogementDateTime from(ValeurCaracteristiqueDemandeReservationLogement valeur) {
			
			
			//Variables pour gerer les messages d'erreurs
		    SessionBag currentSession =  new SessionBag() ;
		    Locale langue = SessionUtil.getLocale(currentSession) ;
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
			//Instance que la methode retourne 
			ValeurCaracteristiqueDemandeReservationLogementDateTime  rtn =  new ValeurCaracteristiqueDemandeReservationLogementDateTime();
			
			//Recuperation de la valeur de la caracteristique 
			String valeurTexte = valeur.getValeurTexte() ;
			
			//Verification de la variable valeurAconvertir
			if 	( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
				
				//Conversion de la variable valeurTexte en LocalDateTime
				LocalDateTime valeurDateTime = LocalDateTime.parse(valeurTexte ,  DateTimeFormatter.ISO_LOCAL_DATE_TIME) ;
				
				//Verification du type de la valeur convertie
					
					try {
							if(!(valeurDateTime instanceof  LocalDateTime ) ) return null ;
						}
						catch(IllegalArgumentException ex) {
						
							 String msg  = MessageTranslationUtil.translate(langue,
									 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
									 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
										new String[] {}) ;
							 
						     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						     
						     return null ;
						} 
	
				rtn.setId(valeur.getId());
					
				rtn.setCaracteristiqueDemandeReservationLogement(valeur.getCaracteristiqueDemandeReservationLogement());
					
				//rtn.setDesignation(valeur.getDesignation());
				
				rtn.setDesignation(valeur.getDesignation());

					
				rtn.setDescription(valeur.getDescription());		
					
				rtn.setValeur(valeurDateTime);
				
				rtn.setVersion(valeur.getVersion());
		
				return rtn ;
			
		}
	

public static ValeurCaracteristiqueDemandeReservationLogement to(ValeurCaracteristiqueDemandeReservationLogementDateTime entity) {
		
		
		
		//Variables pour gerer les messages d'erreurs
		SessionBag currentSession =  new SessionBag() ;
		Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		
		//Instance que la methode retourne
		
		ValeurCaracteristiqueDemandeReservationLogement rtn = new ValeurCaracteristiqueDemandeReservationLogement() ;
		 
		if(entity.getValeur() == null) return null ;
		
		//Conversion de la valeur en texte
		
		 String valeur = entity.getValeur().toString() ;
		
		
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
		  rtn.setCaracteristiqueDemandeReservationLogement(entity.getCaracteristiqueDemandeReservationLogement());
		  rtn.setValeurTexte(valeur);

		  return rtn;
		
	  }

}
