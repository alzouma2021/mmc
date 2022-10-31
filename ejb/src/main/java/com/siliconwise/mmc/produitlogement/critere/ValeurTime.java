package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
 * @date 12/01/2021
 *
 */
public class ValeurTime extends Valeur<LocalTime> implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private List<LocalTime>  valeur ;

	public ValeurTime() {
		super();
	}


public List<LocalTime> getValeur() {
		return valeur;
	}

public void setValeur(List<LocalTime> valeur) {
		this.valeur = valeur;
	}


@SuppressWarnings({ "rawtypes", "unchecked" })
public static ValeurTime from(Valeur<Object> valeur) {
		
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Variables pour stocker les valeurs de type LocalDate
		ArrayList rslt = new ArrayList();
		ArrayList<LocalTime> val = new ArrayList<LocalTime>();
		
		//Instance de la classe ValeurTime que retourne la methode from
		ValeurTime rtn = new ValeurTime();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		//Verification de la variable valeurAconvertir
		
		if(valeurAconvertir == null) return null ;
		
			//Boucle pour convertir la variable valeurAconvertir en LocalDateTime
			for(String value: valeurAconvertir) val.add(LocalTime.parse(value ,  DateTimeFormatter.ISO_LOCAL_TIME)) ;
		
			//Boucle pour verifier le type des valeurs converties
			for(LocalTime value1: val){
				
				try {
					
					   if(!(value1 instanceof  LocalTime ) ) return null ;
						
					}
					catch(IllegalArgumentException ex) {
					
						 String msg  = MessageTranslationUtil.translate(langue,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
								 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
									new String[] {}) ;
						 
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					     
					     return null ;
					} 
			
				rslt.add(value1) ;
			}
			
			rtn.setValeur(rslt);
	
			return rtn ;
		
	}

}
