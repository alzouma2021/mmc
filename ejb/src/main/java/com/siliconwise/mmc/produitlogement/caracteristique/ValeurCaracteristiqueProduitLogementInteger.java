package com.siliconwise.mmc.produitlogement.caracteristique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.Valeur;
import com.siliconwise.mmc.produitlogement.critere.ValeurInteger;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
@Entity
@Table(name="valeurcaracteristiqueproduitlogementinteger")
@DiscriminatorValue("integer")
public class ValeurCaracteristiqueProduitLogementInteger extends CaracteristiqueProduitLogement
		implements Serializable , IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;

	public static final String CODE_TRADUCTION_TYPE_VALEUR_NON_INTEGER = "entite.typeValeur.non-integer";

	@NotNull
	@Column(nullable = false)
	private Integer valeur;

	public ValeurCaracteristiqueProduitLogementInteger() {
		super();
	}

	public Integer getValeur() {
		return valeur;
	}

	public void setValeur(Integer valeur) {
		this.valeur = valeur;
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
		
	}
	
	
public static ValeurCaracteristiqueProduitLogementInteger from(CaracteristiqueProduitLogement valeur) {
		
	
		//Variables pour gerer les messages d'erreurs
	    SessionBag currentSession =  new SessionBag() ;
	    Locale langue = SessionUtil.getLocale(currentSession) ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		
		//Recuperation de la valeur du critere
		 String valeurTexte = valeur.getValeurTexte();
		
		//Instance de la classe ValeurCaracteristiqueProduitLogementInteger que retourne la methode from
		 ValeurCaracteristiqueProduitLogementInteger  rtn = new ValeurCaracteristiqueProduitLogementInteger();
		
		//Verification de la variable valeurAconvertir
		 if( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
		
		//Conversion et verification du type de la valeur
			
			try {
				
				if(!(Integer.valueOf(valeurTexte) instanceof Integer ) ) return null ;
				
				}
				catch(Exception ex) {
					
					String msg  = MessageTranslationUtil.translate(langue,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
						 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
							new String[] {}) ;
					
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
				} 
	
		rtn.setId(valeur.getId());
			
		rtn.setProprieteProduitLogement(valeur.getProprieteProduitLogement());
			
		//rtn.setDesignation(valeur.getDesignation());
		
		rtn.setDesignation( (rtn.getDesignation() == null || rtn.getDesignation() == "" ) 
				? valeur.getProprieteProduitLogement().getDesignation()
				: valeur.getDesignation());

			
		rtn.setDescription(valeur.getDescription());
		
		rtn.setVersion(valeur.getVersion());
			
		rtn.setValeur(Integer.valueOf(valeurTexte));
		
		return rtn ; 
		
	}



public static CaracteristiqueProduitLogement to(ValeurCaracteristiqueProduitLogementInteger entity) {
	
	
	
	//Variables pour gerer les messages d'erreurs
	SessionBag currentSession =  new SessionBag() ;
	Locale langue = SessionUtil.getLocale(currentSession) ;
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
	
	
	//Instance que la methode retourne
	
	CaracteristiqueProduitLogement rtn = new CaracteristiqueProduitLogement() ;
	 
	if(entity.getValeur() == null) return null ;
	
	//Conversion de la valeur en texte
	
	 String valeur = String.valueOf(entity.getValeur()) ;
	
	
		try {
			
			if(!(valeur instanceof  String ) ) return null ;
			
		}
		catch(IllegalArgumentException ex) {
		
			 String msg  = MessageTranslationUtil.translate(langue,
					 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
					 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
						new String[] {}) ;
			 
		     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		     
		     return null ;
		} 

	  rtn.setId(entity.getId());
	  rtn.setDescription(entity.getDescription());
	  rtn.setDesignation(entity.getDesignation());
	  rtn.setVersion(entity.getVersion());
	  rtn.setProprieteProduitLogement(entity.getProprieteProduitLogement());
	  rtn.setValeurTexte(valeur);

	  return rtn;
	
  }
	
}
