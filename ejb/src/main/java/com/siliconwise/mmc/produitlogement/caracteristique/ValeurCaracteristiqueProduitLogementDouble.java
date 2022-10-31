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
import com.siliconwise.common.entity.IEntityMsgVarMap ;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.critere.Valeur;
import com.siliconwise.mmc.produitlogement.critere.ValeurDouble;
/**
 * 
 *
 * @author Alzouma Moussa Mahamadou
 * @date  12/01/2021
 *
 */
@Entity
@Table(name="valeurcaracteristiqueproduitlogementdouble")
@DiscriminatorValue("double")
public class ValeurCaracteristiqueProduitLogementDouble extends CaracteristiqueProduitLogement implements Serializable,IEntityMsgVarMap{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull @Column(nullable=false)
	private Double valeur =  null ;

	public ValeurCaracteristiqueProduitLogementDouble() {
		super();
	}

	public Double getValeur() {
		return valeur;
	}

	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	
	

	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static ValeurCaracteristiqueProduitLogementDouble from(CaracteristiqueProduitLogement valeur) {
			
		
			//Variables pour gerer les messages d'erreurs
		    SessionBag currentSession =  new SessionBag() ;
		    Locale langue = SessionUtil.getLocale(currentSession) ;
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
			
			
			
			//Instance de la classe ValeurDouble que retourne la methode from
			ValeurCaracteristiqueProduitLogementDouble rtn = new ValeurCaracteristiqueProduitLogementDouble();
			
			//Recuperation de la valeur de la caracteristique 
			 String valeurTexte = valeur.getValeurTexte() ;
			
			//Verification de la variable valeurAconvertir
			 if( valeurTexte == null || valeurTexte.isEmpty() ) return null ;
				
				//Boucle pour convertir la variable valeurAconvertir en Double
				Double valeurDouble = Double.parseDouble(valeurTexte) ;
			
					
					try {
							if(!(valeurDouble instanceof  Double ) ) return null ;
						}
						catch(IllegalArgumentException ex) {
						
							 String msg  = MessageTranslationUtil.translate(langue,
									 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE,
									 	AppMessageKeys.CODE_TYPE_VALEUR_PROPRIETE_NON_VALIDE, 
										new String[] {}) ;
							 
						     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						     
						     return null ;
						} 
					
				rtn.setId(valeur.getId());
					
				rtn.setProprieteProduitLogement(valeur.getProprieteProduitLogement());
					
				//rtn.setDesignation(valeur.getDesignation());
				
				rtn.setDesignation( (rtn.getDesignation() == null || rtn.getDesignation() == "" ) 
						? valeur.getProprieteProduitLogement().getDesignation()
						: valeur.getDesignation());

				rtn.setDescription(valeur.getDescription());
				
				rtn.setVersion(valeur.getVersion());
				
				rtn.setValeur(valeurDouble);
		
				return rtn ;
			
		}


public static CaracteristiqueProduitLogement to(ValeurCaracteristiqueProduitLogementDouble entity) {
		
		
		
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
