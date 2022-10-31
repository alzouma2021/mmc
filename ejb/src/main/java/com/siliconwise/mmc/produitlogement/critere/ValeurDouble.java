package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
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
public class ValeurDouble extends Valeur<Double> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;

	private List<Double>valeur = null;

	public ValeurDouble() {
		super();

	}

public List<Double> getValeur() {
		return valeur;
	}

public void setValeur(List<Double> valeur) {
		this.valeur = valeur;
	}

@SuppressWarnings({ "unchecked", "rawtypes"})
public static ValeurDouble from(Valeur<Object> valeur) {
		
	
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		
		
		//Variables pour stocker les valeurs de type Double
		ArrayList rslt = new ArrayList();
		
		ArrayList<Double> val = new ArrayList<Double>();
		
		//Instance de la classe ValeurDouble que retourne la methode from
		ValeurDouble rtn = new ValeurDouble();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		
		//Verification de la variable valeurAconvertir
		if(valeurAconvertir == null ) return null ;
			
			//Boucle pour convertir la variable valeurAconvertir en Double
			for(String value: valeurAconvertir) val.add(Double.parseDouble(value)) ;
			
		
			//Boucle pour verifier le type des valeurs converties
			for(Double value1: val){
				
				try {
						if(!(value1 instanceof  Double ) ) return null ;
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
