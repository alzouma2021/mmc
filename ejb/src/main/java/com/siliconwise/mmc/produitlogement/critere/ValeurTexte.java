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
public class ValeurTexte extends Valeur<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> valeur ;

	public ValeurTexte() {
		super();
	}

	
public List<String> getValeur() {
		return valeur;
	}

public void setValeur(List<String> valeur) {
		this.valeur = valeur;
	}


public static ValeurString from(Valeur<Object> valeur) {
		
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte();
		
		//Variable pour stocker les valeurs de type Texte
		ArrayList<String> addList = new ArrayList<String>();
		
		//Instance de la classe ValeurTexte que retourne la methode from
		ValeurString rtn = new ValeurString();
		
		//Verification de la variable valeurAconvertir
		if( valeurAconvertir == null ) return null ;
		
		//Boucle pour conertir et  verifier le type des valeurs converties
		for(String valueString: valeurAconvertir) {
			
			try {
				
				   if(!(valueString instanceof String ) ) return null ;
					
				}
				catch(Exception ex) {
					
					String msg  = MessageTranslationUtil.translate(langue,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
							new String[] {}) ;
					
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
				}
			
				addList.add(valueString);
			}
		
			rtn.setValeur(addList);
	
			return rtn ;
			
	}
}
