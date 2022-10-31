package com.siliconwise.mmc.produitlogement.caracteristique;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;

import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.logement.Logement;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogement;

/**
 * Cette classe renferme les valeurs de propriétés de produit logement
 * 
 * Identité Domaniale : designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 08/01/2021
 * 
 * 
 */
@NamedQueries(value={
		
		/*Retourne l'id d'une caracteristique en fonction sa designation et du code produit logement*/
	
		@NamedQuery(
				name="caracteristiqueProduitLogementIdParDesignationParProduitLogement",
				query="SELECT DISTINCT cpl.id FROM CaracteristiqueProduitLogement cpl "
						+ "WHERE cpl.designation = :designation "
						+ "AND  cpl.produitLogement.code = :produitLogementCode ") ,
		
		
		/*Retourne l'id d'une caracteristique en fonction sa designation et de la designation  logement*/
		
		@NamedQuery(
				name="caracteristiqueLogementIdParDesignationParLogement",
				query="SELECT DISTINCT cpl.id FROM CaracteristiqueProduitLogement cpl "
						+ "WHERE cpl.designation = :designation "
						+ "AND  cpl.logement.designation = :logementDesignation ") ,
		
		
		/*Retourne toutes les caracteristiques d'un produit logement donné*/
		
		@NamedQuery(
				name="caracteristiquesParProduitLogement",
				query="SELECT cpl FROM CaracteristiqueProduitLogement cpl " 
						  + "WHERE cpl.produitLogement.id = :idProduit ") 
		
})
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="caracteristiqueproduitlogement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class CaracteristiqueProduitLogement implements Serializable , IEntityStringkey , IEntityMsgVarMap{

	
	
	private static final long serialVersionUID = 1L;


	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille max de l'Id est de 50")
	private String id;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 )
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;

	// ManyToOne CaracteristiqueProduitLogement vers ProprieteProduitLogement
	@ManyToOne
	@NotNull(message = "jpa_not_null_VarN")
	private ProprieteProduitLogement proprieteProduitLogement ;
	
	//ManyToOne CaracteristiqueProduitLogement vers ProduitLogement
	@ManyToOne
	private ProduitLogement  produitLogement ;
	
	
	
	//ManyToOne CaracteristiqueProduitLogement vers Logement
	@ManyToOne
	private Logement  logement ;
	
	
	//Contient en type String , la valeur de la caracteristiquue envoyée par le front end
	@Transient
	private String valeurTexte ;
	
	
	@Version
	private Integer version;

	public CaracteristiqueProduitLogement() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProprieteProduitLogement getProprieteProduitLogement() {
		return proprieteProduitLogement;
	}

	public void setProprieteProduitLogement(ProprieteProduitLogement proprieteProduitLogement) {
		this.proprieteProduitLogement = proprieteProduitLogement;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public ProduitLogement getProduitLogement() {
		return produitLogement;
	}

	public void setProduitLogement(ProduitLogement produitLogement) {
		this.produitLogement = produitLogement;
	}
	

	public String getValeurTexte() {
		return valeurTexte;
	}

	public void setValeurTexte(String valeurTexte) {
		this.valeurTexte = valeurTexte;
	}
	
	public Logement getLogement() {
		return logement;
	}

	public void setLogement(Logement logement) {
		this.logement = logement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaracteristiqueProduitLogement other = (CaracteristiqueProduitLogement) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	*/
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, "Caracteristique du logement");
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDescription());
		
		return rtn;
	}
	
	

	public boolean estCaracteristiqueProduitLogement(
						Set<CaracteristiqueProduitLogement> entityList) {
		
		//Vérifier l'entité
		
		if( entityList == null) return false ;
		
		//Pour chaque caracteristique dont l'attribut estObligatoire de la propriété est true
		//Alors verifie si la valeur est non nulle 
		//si la valeur est nulle alors renvoie un message d'erreurs
		
		for(CaracteristiqueProduitLogement entity: entityList) {
			
			
			if(entity != null && entity.getProprieteProduitLogement() != null
							  && entity.getProprieteProduitLogement().getEstObligatoire() != null
							  && entity.getProprieteProduitLogement().getEstObligatoire() == true ) {
				
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementInteger) {
					
					ValeurCaracteristiqueProduitLogementInteger	entityInteger = (ValeurCaracteristiqueProduitLogementInteger) entity;
					
					if( entityInteger.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementBoolean) {
					
					ValeurCaracteristiqueProduitLogementBoolean	entityBoolean = (ValeurCaracteristiqueProduitLogementBoolean) entity;
					
					if( entityBoolean.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementLong) {
					
					ValeurCaracteristiqueProduitLogementLong	entityLong = (ValeurCaracteristiqueProduitLogementLong) entity;
					
					if( entityLong.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementDouble) {
					
					ValeurCaracteristiqueProduitLogementDouble	entityDouble = (ValeurCaracteristiqueProduitLogementDouble) entity;
					
					if( entityDouble.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementFloat) {
					
					ValeurCaracteristiqueProduitLogementFloat	entityFloat = (ValeurCaracteristiqueProduitLogementFloat) entity;
					
					if( entityFloat.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementDate) {
					
					ValeurCaracteristiqueProduitLogementDate	entityDate = (ValeurCaracteristiqueProduitLogementDate) entity;
					
					if( entityDate.getValeur() == null) return false ;
					
				}
				
			
				if(entity instanceof ValeurCaracteristiqueProduitLogementDateTime) {
					
					ValeurCaracteristiqueProduitLogementDateTime	entityDateTime = (ValeurCaracteristiqueProduitLogementDateTime) entity;
					
					if( entityDateTime.getValeur() == null) return false ;
					
				}
				
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementTime) {
					
					ValeurCaracteristiqueProduitLogementTime	entityTime = (ValeurCaracteristiqueProduitLogementTime) entity;
					
					if( entityTime.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementDocument) {
					
					ValeurCaracteristiqueProduitLogementDocument	entityDocument = (ValeurCaracteristiqueProduitLogementDocument) entity;
					
					if( entityDocument.getValeur() == null) return false ;
					
				}
				
				if(entity instanceof ValeurCaracteristiqueProduitLogementReference) {
					
					ValeurCaracteristiqueProduitLogementReference	entityReference = (ValeurCaracteristiqueProduitLogementReference) entity;
					
					if( entityReference.getValeur() == null) return false ;
					
				}
				
				
			}
			
			
		}
		

		return true;
	
	}
	
	
}
