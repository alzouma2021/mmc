package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

public class ValeurReference extends Valeur<Reference> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Reference> valeur ;

	public ValeurReference() {
		super();
	}

	
	public List<Reference> getValeur() {
			return valeur ;
		}
	
	public void setValeur(List<Reference> valeur) {
			this.valeur = valeur;
		}
	
	

	public static ValeurReference from(Valeur<Object> valeur) {
			
			//Variables pour gerer les messages d'erreurs 
		    SessionBag currentSession =  new SessionBag() ;
		    Locale langue = SessionUtil.getLocale(currentSession) ;
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
			
			/**
			 * Modification faite par Alzouma date 24/06/21
			 * Optimisation de la methode 
			 * Car la valeur du critere ne contiendra que l'id de la reference en question et non la classe Reference entiere
			 * Donc le type de la valeur est String au lieu de Reference
			 */
			
			
			/*
			List<Reference> valeurAconvertir = valeur.getValeurReference();
			
			
			//Variable pour contenir les valeurs definitives
			ArrayList<Reference> addList = new ArrayList<Reference>();
			
			//Instance de la classe ValeurReference que la methode va retourner
			ValeurReference rtn = new ValeurReference();
			*/
			
			
			
			//Variable Liste qui contiendra la valeur du critere
			
			List<String> valeurAconvertir = valeur.getValeurTexte();
			
			//Variable pour contenir les valeurs definitives
			ArrayList<Reference> addList = new ArrayList<Reference>();
			
			//Instance de la classe ValeurReference que la methode va retourner
			
			Reference reference = new Reference();
			
			ValeurReference rtn = new ValeurReference();
			
			//Verification de la variable valeurAconvertir
			if(valeurAconvertir == null) return null ;
			
			//Boucle pour verifier le type de chaque Objet de la variable convertie
			
			for(String valueReference: valeurAconvertir) {
				
				try {
					
						//Si le type de l'objet ne correspondant pas à Reference alors on retourne null
						if(!(valueReference instanceof String ) ) return null ;
						
					}
					catch(ClassCastException ex) {
						
						String msg  = MessageTranslationUtil.translate(langue,
							 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
							 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
								new String[] {}) ;
						
						msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						
					}
					
					reference.setId(valueReference);
					
					addList.add(reference);
				}
			
				rtn.setValeur(addList);
		
				return rtn ;
				
		}
}
