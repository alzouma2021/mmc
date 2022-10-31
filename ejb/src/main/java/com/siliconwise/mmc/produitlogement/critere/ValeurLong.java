package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class ValeurLong extends Valeur<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Long> valeur;

	public ValeurLong() {
		super();
	}

	
public List<Long> getValeur() {
		return valeur;
	}

public void setValeur(List<Long> valeur) {
		this.valeur = valeur;
	}

@SuppressWarnings({ "unchecked", "rawtypes" })
public static ValeurLong from(Valeur<Object> valeur) {
		
		//Variables de la methode 
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Variables liste pour stocker les valeurs converties
		ArrayList rslt = new ArrayList();
		
		ArrayList<Long> val = new ArrayList<Long>();
		
		//Instance de classe ValeurLong que le retourne la methode
		ValeurLong rtn = new ValeurLong();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		//Verification de la variabnle valeurAconvertir : si nulle retourne nulle
		if( valeurAconvertir == null ) return null ;
			
			//Boucle pour conversion de la variable valeurAconvertir en Long
			for(String value: valeurAconvertir) val.add(Long.parseLong(value)) ;
			
			//Boucle pour verifier si les valeurs converties ont pour type Long
			//Si le type ne correspondant pas à un Long alors , on retourne un  nul
			for(Long value1: val){
				
				try {
						if(!(value1 instanceof  Long ) ) return null ;
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
			
			//Retourne l'instance rtn
			return rtn ;
		
	}

}
