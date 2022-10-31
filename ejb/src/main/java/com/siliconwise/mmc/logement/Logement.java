package com.siliconwise.mmc.logement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;


/**
 * Cette classe renferme les informations relatives aux logemnts
 * 
 * Identite Domaniale : designation  et code Produit Logement
 * 
 * @author Alzouma Moussa Mahamadou
 * @date de creation 25/0/2022
 *
 *
 */
@NamedQueries(value={
		
		/*Recherche de logement id  par designation  et par produit logement code*/
		
		@NamedQuery(
				name= "logementIdParDesignationParProduitLogementCode", 
				query = "SELECT DISTINCT lo.id FROM Logement lo "
						+ "WHERE lo.designation = :designation and lo.produitLogement.code = :produitLogement"),
		
})
@Entity
@Table(name = "logement",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"designation" , "produitLogement_id" })
	})
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Logement implements Serializable, 
										IEntityStringkey, IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
	
	public static final String CODE_TRADUCTION_NOM_USUEL = "logement.nomUsuel" ;
	
	
	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit pas  depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;

	//Cette propriété de type Boolean permet au promoteur d'activer le produit logement pour commercialisatio
	@Column(nullable = true )
	private Boolean estActive = null;
	
	
	// OneToMAny Logement vers CaracteristiqueProduitLogement
	@OneToMany(mappedBy="logement", cascade = (CascadeType.REMOVE) ,fetch = FetchType.EAGER )
	private Set<CaracteristiqueProduitLogement> caracteristiqueLogementList ;
	
	
	@ManyToOne
	private ProduitLogement produitLogement ;
	
	
	@Version
	private Integer version ;
	

	public Logement() {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getEstActive() {
		return estActive;
	}

	public void setEstActive(Boolean estActive) {
		this.estActive = estActive;
	}
	
	public Set<CaracteristiqueProduitLogement> getCaracteristiqueLogementList() {
		return caracteristiqueLogementList;
	}

	public void setCaracteristiqueLogementList(Set<CaracteristiqueProduitLogement> caracteristiqueLogementList) {
		this.caracteristiqueLogementList = caracteristiqueLogementList;
	}

	public ProduitLogement getProduitLogement() {
		return produitLogement;
	}

	public void setProduitLogement(ProduitLogement produitLogement) {
		this.produitLogement = produitLogement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((designation == null) ? 0 : designation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Logement other = (Logement) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Logement [id=" + id + ", designation=" + designation + ", description=" + description + "]";
	}
	

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Logement.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	

}
