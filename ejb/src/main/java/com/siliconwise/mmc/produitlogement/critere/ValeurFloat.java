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
public class ValeurFloat extends Valeur<Float> implements Serializable {

	private static final long serialVersionUID = 1L;

	private  List<Float> valeur ;

	public ValeurFloat() {
		super();
	}


public List<Float> getValeur() {
		return valeur;
	}

public void setValeur(List<Float> valeur) {
		this.valeur = valeur;
	}

@SuppressWarnings({ "unchecked", "rawtypes" })
public static ValeurFloat from(Valeur<Object> valeur) {
		
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Variables pour stocker les valeurs de type Float
		ArrayList rslt = new ArrayList();
		ArrayList<Float> val = new ArrayList<Float>();
		
		//Instance de la classe ValeurFloat que retourne la methode from
		ValeurFloat rtn = new ValeurFloat();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		//Verification de la variable valeurAconvertir
		if(valeurAconvertir == null) return null ;
			
			//Boucle pour convertir la variable valeurAconvertir en Float
			for(String value: valeurAconvertir) val.add(Float.parseFloat(value)) ;
			
			//Boucle pour verifier le type des valeurs converties
			for(Float value1: val){
				
				try {
						if(!(value1 instanceof  Float ) ) return null ;
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
