package com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.Ville;
import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;


/**
 * Cette classe renferme les informations relatives aux type de documents
 *  de demande de réservation de logement , requis pour un porgramme immobilier
 * 
 * Identitté Domaniale :
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 09/09/2022
 *
 *
 */
@NamedQueries(value={
		
		/* ID type de document de demande reservation logement par designation par programme immobilier*/
		
		/*
		@NamedQuery(
				name= "typeDocumentDemandeReservationLogementParIdParDesignationParProgramme", 
				query = "SELECT DISTINCT tddrl.id FROM TypeDocumentDemandeReservationLogement tddrl "
						+ "WHERE tddrl.designation = :designation "
						+ "AND tddrl.programmeImmobilier.code = :code")*/
})
@Entity
@Table(name = "typedocumentdemandereservationlogement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class TypeDocumentDemandeReservationLogement implements Serializable, IEntityStringkey, IEntityMsgVarMap {

	
	
	private static final long serialVersionUID = 1L;

	private static final String CODE_TRADUCTION_NOM_USUEL = null ;

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;


	//ManyToOne ProgrammeImmobilier vers Promoteur
	@ManyToOne
	@NotNull(message = "Le programme immobilier ne doit pas être nul")
	private ProgrammeImmobilier programmeImmobilier ;
	
	
	//ManyToOne
	@NotNull(message = "La reference ne doit pas être nulle")
	@OneToOne
	private Reference typeDocument ;
	
	@Version
	private Integer version ;

	public TypeDocumentDemandeReservationLogement() {
		super();
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ProgrammeImmobilier getProgrammeImmobilier() {
		return programmeImmobilier;
	}

	public void setProgrammeImmobilier(ProgrammeImmobilier programmeImmobilier) {
		this.programmeImmobilier = programmeImmobilier;
	}

	public Reference getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(Reference type) {
		this.typeDocument = type;
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
		result = prime * result + ((typeDocument == null) ? 0 : typeDocument.hashCode());
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
		TypeDocumentDemandeReservationLogement other = (TypeDocumentDemandeReservationLogement) obj;
		if (programmeImmobilier == null) {
			if (other.programmeImmobilier != null)
				return false;
		} else if (!programmeImmobilier.equals(other.programmeImmobilier))
			return false;
		if (typeDocument == null) {
			if (other.typeDocument != null)
				return false;
		} else if (!typeDocument.equals(other.typeDocument))
			return false;
		return true;
	}


	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, TypeDocumentDemandeReservationLogement.CODE_TRADUCTION_NOM_USUEL) ;
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
	
}
