package com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;


/**
 * Cette classe renferme les informations relatives aux caracteristiques de demande de réservation de logement d'un programme immobilier
 * 
 * Identitté Domaniale :
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 09/09/2022
 *
 *
 */
@NamedQueries(value={
		
		/* ID Caracteristique de demande reservation logement par designation par programme immobilier*/
		
		/*
		@NamedQuery(
				name= "caracteristiqueDemandeReservationLogementIdParDesignationParProgramme", 
				query = "SELECT DISTINCT cdrl.id FROM CaracteristiqueDemandeReservationLogement cdrl "
						+ "WHERE cdrl.designation = :designation "
						+ "AND cdrl.programmeImmobilier.code = :code "),*/
})
@Entity
@Table(name = "caracteristiquedemandereservationlogement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class CaracteristiqueDemandeReservationLogement implements Serializable, IEntityStringkey, IEntityMsgVarMap {


	private static final long serialVersionUID = 1L;

	private static final String CODE_TRADUCTION_NOM_USUEL = null ;

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;
	
	//Cette propriété de type de Boolean de savoir si la caracteristique est obligatoire ou non.

	private boolean estObligatoire ;

	//ManyToOne ProgrammeImmobilier vers Promoteur
	@NotNull(message = "Le programme immmobilier ne doit pas être nulle")
	@ManyToOne
	@NotNull(message = "Le programme immobilier ne doit pas être nul")
	private ProgrammeImmobilier programmeImmobilier ;
	
	
	//ManyToOne
	@NotNull(message = "La reference ne doit pas être nulle")
	@OneToOne
	private Reference typeCaracteristique ;
	
	@Version
	private Integer version ;

	public CaracteristiqueDemandeReservationLogement() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEstObligatoire() {
		return estObligatoire;
	}

	public void setEstObligatoire(boolean estObligatoire) {
		this.estObligatoire = estObligatoire;
	}

	public ProgrammeImmobilier getProgrammeImmobilier() {
		return programmeImmobilier;
	}

	public void setProgrammeImmobilier(ProgrammeImmobilier programmeImmobilier) {
		this.programmeImmobilier = programmeImmobilier;
	}

	public Reference getTypeCaracteristique() {
		return typeCaracteristique;
	}

	public void setTypeCaracteristique(Reference type) {
		this.typeCaracteristique = type;
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
		result = prime * result + ((programmeImmobilier == null) ? 0 : programmeImmobilier.hashCode());
		result = prime * result + ((typeCaracteristique == null) ? 0 : typeCaracteristique.hashCode());
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
		CaracteristiqueDemandeReservationLogement other = (CaracteristiqueDemandeReservationLogement) obj;
		if (programmeImmobilier == null) {
			if (other.programmeImmobilier != null)
				return false;
		} else if (!programmeImmobilier.equals(other.programmeImmobilier))
			return false;
		if (typeCaracteristique == null) {
			if (other.typeCaracteristique != null)
				return false;
		} else if (!typeCaracteristique.equals(other.typeCaracteristique))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaracteristiqueDemandeReservationLogement [estObligatoire=" + estObligatoire + ", programmeImmobilier="
				+ programmeImmobilier + ", typeCaracteristique=" + typeCaracteristique + ", version=" + version + "]";
	}


	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, CaracteristiqueDemandeReservationLogement.CODE_TRADUCTION_NOM_USUEL) ;
	//	rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
	
}
