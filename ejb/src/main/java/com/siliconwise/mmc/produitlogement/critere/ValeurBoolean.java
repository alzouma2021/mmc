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
 * @date 11/01/2021
 *
 */

public class ValeurBoolean extends Valeur<Boolean> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Boolean> valeur;

	public ValeurBoolean() {
		super();
	}
	
	public void setValeur(List<Boolean> valeur) {
		this.valeur = valeur;
	}

	@Override
	public List<Boolean> getValeur() {
		return (List<Boolean>) valeur;
	}

@SuppressWarnings({ "rawtypes", "unchecked" })
public static ValeurBoolean from(Valeur<Object> valeur) {
		
		//Variables pour gerer les messages d'erreurs
		SessionBag currentSession =  new SessionBag() ;
		Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Varaible qui contiendra les valeurs definitives 
		ArrayList rslt = new ArrayList();
		
		//Variable qui contiendra les valeurs de la variable valeurAconvertir converties en Boolean
		ArrayList<Boolean> val = new ArrayList<Boolean>();
		
		//Instance de la classe que la methode va retourner
		ValeurBoolean rtn = new ValeurBoolean();
		
		//Variable list qui contient la valeur liste du critere
		List<String> valeurAconvertir = valeur.getValeurTexte() ;
		
		//Verification si la valeurAconvertir est nulle
		if 	( valeurAconvertir == null ) return null ;
		
		//Boucle pour convertir la variable valeurAconvertir en Boolean
		for(String value: valeurAconvertir) val.add(Boolean.parseBoolean(value)) ;
			
			//Boucle pour verifier le type des valeurs converties
			//Si le type ne correspond à Boolean alors on retourne un null
			for(Boolean value1: val){
				
				try {
						
						if(!(value1 instanceof  Boolean ) ) return null ;
						
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
