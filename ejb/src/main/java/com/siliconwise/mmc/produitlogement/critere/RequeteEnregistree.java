package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


import javax.validation.constraints.Size;


import com.siliconwise.common.entity.IEntityStringkey;


/**
 * 
 * Identite Domaniale : designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
public class RequeteEnregistree implements Serializable, IEntityStringkey {

	private static final long serialVersionUID = 1L;

	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;

	 @Size(max = 150, message = "La taille max est dépassée")
	private String designation = null;

	private String description = null;

	private Set<CritereRechercheProduitLogement> critereList = new HashSet<CritereRechercheProduitLogement>();

	public RequeteEnregistree() {
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

	public Set<CritereRechercheProduitLogement> getCritereList() {
		return critereList;
	}

	public void setCritereList(Set<CritereRechercheProduitLogement> critereList) {
		this.critereList = critereList;
	}

	// Methode addToCritereList
	public void addToCritereList(CritereRechercheProduitLogement critere) {

		if (critere == null)
			return;
		if (!critereList.contains(critere)) {
			critereList.add(critere);
		}
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
		RequeteEnregistree other = (RequeteEnregistree) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequeteEnregistree [designation=" + designation + "]";
	}

	@Override
	public Integer getVersion() {
		
		
		return null;
	}

	@Override
	public void setVersion(Integer version) {
	

	}
}
