package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.siliconwise.common.Ville;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

public class ValeurVille extends Valeur<Ville> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Ville> valeur ;

	public ValeurVille() {
		super();
	}

	
	public List<Ville> getValeur() {
			return valeur ;
		}
	
	public void setValeur(List<Ville> valeur) {
			this.valeur = valeur;
		}
	
	
	public static ValeurVille from(Valeur<Object> valeur) {
			
			//Variables pour gerer les messages d'erreurs 
		
		    SessionBag currentSession =  new SessionBag() ;
		    Locale langue = SessionUtil.getLocale(currentSession) ;
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
			//Variable Liste qui contiendra la valeur du critere
			//Les valeurs contiendront les Id des villes selectionnées donc de type String
			
			List<String> valeurAconvertir = valeur.getValeurTexte();
		
			//Variable pour contenir les valeurs definitives
			
			ArrayList<Ville> addList = new ArrayList<Ville>();
			
			//Instance de la classe ValeurVille que la methode va retourner
			
			Ville ville = new Ville();
			
			ValeurVille rtn = new ValeurVille();
			
			//Verification de la variable valeurAconvertir
			
			if(valeurAconvertir == null) return null ;
			
			//Boucle pour verifier le type de chaque Objet de la variable valConertie 
			
			for(String valeurVille: valeurAconvertir) {
				
				try {
					    //Verification du type de la variable
						if(!(valeurVille instanceof String ) ) return null ;
						
					}
					catch(ClassCastException ex) {
						
						String msg  = MessageTranslationUtil.translate(langue,
							 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
							 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
								new String[] {}) ;
						
						msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						
					}
					
					ville.setId(valeurVille);
					
					addList.add(ville);
					
				}
			
				rtn.setValeur(addList);
		
				return rtn ;
				
		}
	
	
}
