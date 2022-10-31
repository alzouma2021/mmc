package com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * 
 * Valeur de la caracteristique du produit logement document
 * 
 * @author Alzouma Moussa Mahamadou
 * @date  01/02/2021
 *
 */
@NamedQueries(value={
		@NamedQuery(
				name="valeurCaracteristiqueDemandeReservationLogementDocumentParDesignation",
				query="SELECT vcdrlDocument FROM ValeurCaracteristiqueDemandeReservationLogementDocument vcdrlDocument  "
					+ "WHERE vcdrlDocument.designation = :designation")
})
@Entity
@Table(name="valeurcaracteristiquedemandereservationlogementdocument")
@DiscriminatorValue("document")
public class ValeurCaracteristiqueDemandeReservationLogementDocument extends ValeurCaracteristiqueDemandeReservationLogement 
														  implements Serializable , IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;
	
	//Relation entre ValeurCaracteristiqueProduitLogementDocument et Document
	@ManyToOne
	@NotNull
	private Document valeur ;
	
	
	public Document getValeur() {
		return valeur;
	}

	public void setValeur(Document valeur) {
		this.valeur = valeur;
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}

	
}
