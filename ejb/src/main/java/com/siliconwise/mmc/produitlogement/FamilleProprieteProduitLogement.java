package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

/**
 * cette classe renferme les informations relatives aux familles Proprétés
 * Produit Logement
 * 
 * Identité Domaniale : designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 *
 */
@Entity
@Table(name = "familleproprieteproduitlogement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class FamilleProprieteProduitLogement implements Serializable, IEntityStringkey {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "La designation ne doit pas être vide")
	@Column(length = 150, unique = true)
	@Size(max = 150, message = "La taille ne doit pas dépasser 150 caratères")
	private String designation = null;

	@Lob
	private String description = null;
	
	@Version
	private Integer version ;
	
	public FamilleProprieteProduitLogement() {
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
		FamilleProprieteProduitLogement other = (FamilleProprieteProduitLogement) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FamilleProprieteProduitLogement [designation=" + designation + "]";
	}
}
