package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.AppUtil;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
public class ValeurInteger extends Valeur<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private List<Integer> valeur;
	
	public List<Integer> getValeur() {
		return valeur;
	}

	public void setValeur(List<Integer> valeur) {
		this.valeur = valeur;
	}


	public ValeurInteger() {
		super();
	}

@SuppressWarnings({ "unchecked", "rawtypes" })
public static ValeurInteger from(Valeur<Object> valeur) {
		
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Recuperation de la valeur du critere
		List<String> valeurAconvertir = valeur.getValeurTexte();
		
		//Variable pour stocker les valeurs de type Integer
		ArrayList addList = new ArrayList();
		
		//Instance de la classe ValeurInteger que retourne la methode from
		ValeurInteger  rtn = new ValeurInteger();
		
		//Verification de la variable valeurAconvertir
		if (valeurAconvertir == null) return null ;
		
		//Boucle pour conertir et  verifier le type des valeurs converties
		for(String  val: valeurAconvertir){
			
			try {
				
				if(!(Integer.valueOf(val) instanceof Integer ) ) return null ;
				
				}
				catch(Exception ex) {
					
					String msg  = MessageTranslationUtil.translate(langue,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
							new String[] {}) ;
					
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
				} 
		
			addList.add((Integer.valueOf(val)));
		}
		
		rtn.setValeur(addList);
		
		return rtn ; 
		
	}
	
}
