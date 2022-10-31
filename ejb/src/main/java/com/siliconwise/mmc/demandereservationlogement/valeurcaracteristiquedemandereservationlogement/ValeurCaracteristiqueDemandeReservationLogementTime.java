package com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.time.LocalTime;
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
import com.siliconwise.mmc.produitlogement.critere.Valeur;
import com.siliconwise.mmc.produitlogement.critere.ValeurTime;

/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 * @date  12/01/2021
 * 
 * 
 */
@Entity
@Table(name="valeurcaracteristiquedemandereservationlogementtime")
@DiscriminatorValue("time")
public class ValeurCaracteristiqueDemandeReservationLogementTime extends ValeurCaracteristiqueDemandeReservationLogement implements Serializable , IEntityMsgVarMap{
	
	private static final long serialVersionUID = 1L;
    
	@NotNull @Column(nullable = false)
	private LocalTime valeur = null ;

	public ValeurCaracteristiqueDemandeReservationLogementTime() {
		super();
	}

	public LocalTime getValeur() {
		return valeur;
	}

	public void setValeur(LocalTime valeur) {
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
	public static ValeurCaracteristiqueDemandeReservationLogementTime from(ValeurCaracteristiqueDemandeReservationLogement valeur) {
			
			//Variables pour gerer les messages d'erreurs
		    SessionBag currentSession =  new SessionBag() ;
		    Locale langue = SessionUtil.getLocale(currentSession) ;
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
			//Instance de la classe ValeurCaracteristiqueProduitLogementTime que retourne la methode from
			ValeurCaracteristiqueDemandeReservationLogementTime rtn = new ValeurCaracteristiqueDemandeReservationLogementTime();
			
			//Recuperation de la valeur de la caracteristique
			String valeurTexte = valeur.getValeurTexte() ;
			
			//Verification de la variable valeurAconvertir
			if( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
			
				//Conersionla variable valeurTexte en LocalDateTime
				 LocalTime valeurTime = LocalTime.parse(valeurTexte ,  DateTimeFormatter.ISO_LOCAL_TIME) ;
			
				//Verification du type de la valeur convertie
				 
					try {
						
						   if(!(valeurTime instanceof  LocalTime ) ) return null ;
							
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
				
				rtn.setVersion(valeur.getVersion());
				
				rtn.setValeur(valeurTime);
		
				return rtn ;
			
		}
	

	public static ValeurCaracteristiqueDemandeReservationLogement to(ValeurCaracteristiqueDemandeReservationLogementTime entity) {
		
		
		
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
