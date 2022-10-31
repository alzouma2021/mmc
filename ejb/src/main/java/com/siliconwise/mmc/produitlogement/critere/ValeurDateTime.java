package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
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
public class ValeurDateTime extends Valeur<LocalDateTime> implements Serializable {

	
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	private List<LocalDateTime>  valeur ;

	public ValeurDateTime() {
		super();

	}


public List<LocalDateTime> getValeur() {
		return valeur;
	}

public void setValeur(List<LocalDateTime> valeur) {
		this.valeur = valeur;
	}


@SuppressWarnings({ "rawtypes", "unchecked" })
public static ValeurDateTime from(Valeur<Object> valeur) {
		
		
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Variables pour stocker les valeurs de type LocalDate
		
		ArrayList rslt = new ArrayList();
		ArrayList<LocalDateTime> val = new ArrayList<LocalDateTime>();
		
		//Instance de la classe ValeurDateTime que retourne la methode from
		
		ValeurDateTime rtn = new ValeurDateTime();
		
		//Recuperation de la valeur du critere
		
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		//Verification de la variable valeurAconvertir
		
		if 	( valeurAconvertir == null ) return null ;
			
			//Boucle pour convertir la variable valeurAconvertir en LocalDateTime
		
			for(String value: valeurAconvertir) val.add(LocalDateTime.parse(value ,  DateTimeFormatter.ISO_OFFSET_DATE_TIME)) ;
			
			//Boucle pour verifier le type des valeurs converties
			
			for(LocalDateTime value1: val){
				
				try {
					
						if(!(value1 instanceof  LocalDateTime ) ) return null ;
						
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
