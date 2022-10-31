package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;

import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;

/**
 * 
 * Classe d'association ProduitLogementDocument
 * @author Alzouma Moussa Mahamadou
 */
@NamedQueries(value = {
		
	
		/* Recherche de la liste de video avec metadatas d'un produit logement*/
		
		@NamedQuery(
				name= "rechercherDocumentListParProgrammeImmobilierParTypeDocument", 
				query = "SELECT  pid.document FROM ProgrammeImmobilierDocument pid "
						+ " WHERE pid.programmeImmobilier.id = :idProgrammeImmobilier "
						+ " AND pid.document.typeDocument.id = :idTypeDocument "),
		
		/*Recherche de documents appartenant au programme immobilier */
		
		@NamedQuery(
				name= "rechercherDocumentListParIdProgrammeImmobilier", 
				query = "SELECT  pid.document FROM ProgrammeImmobilierDocument pid "
						+ " WHERE pid.programmeImmobilier.id = :idProgrammeImmobilier " ),
		
		/*Suppression des inofmrations de la classe d'association ProgrammeImmobilierDocument */
		
		@NamedQuery(
				name= "suppressionInformationsProgrammeImmobilierDocument", 
				query = "DELETE  FROM ProgrammeImmobilierDocument pid "
						+ " WHERE pid.programmeImmobilier.id = :idProgrammeImmobilier  "),
		
		
       /* Supprimer un document par son id dans la classe d'association programmeImmobilierDocument */
		
		@NamedQuery(
				name= "supprimerProgrammeImmobilierDocumentParId", 
				query = "DELETE  FROM ProgrammeImmobilierDocument pid "
						+ " WHERE pid.document.id = :idDocument ")
		
	})
@Entity
@Table(name="programmeimmobilierdocument")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ProgrammeImmobilierDocument implements IEntityStringkey, Serializable, IEntityMsgVarMap {

	
	private static final long serialVersionUID = -7677070722719366509L;
	
	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;
	
	
	@ManyToOne
	@NotNull(message="Le programme immobilier ne doit pas être nul")
	private ProgrammeImmobilier programmeImmobilier = null;
	
	
	@ManyToOne
	@NotNull(message="Le document ne doit pas être nul")
	private Document document = null ;
	

	@Version
	private Integer version = null ;
	
	
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}


	@Override
	public String toString() {
		return "ProgrammeImmobilierDocument [programmeImmobilier=" + programmeImmobilier + ", document=" + document
				+ "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Reference.CODE_TRADUCTION_NOM_USUEL) ;
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
	
}
