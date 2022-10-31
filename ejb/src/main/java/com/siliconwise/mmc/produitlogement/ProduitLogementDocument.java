package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
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


/**
 * 
 * Classe d'association ProduitLogementDocument
 * @author Alzouma Moussa Mahamadou
 */
@NamedQueries(value = {
		
		/* Recherche d'image de consultation d'un produit logement  */
		
		@NamedQuery(
			name= "rechercherDocumentParProduiLogementParTypeDocument", 
			query = "SELECT  pld.document FROM ProduitLogementDocument pld "
					+ " WHERE pld.produitLogement.id = :idProduitLogement "
					+ " AND pld.document.typeDocument.id = :idTypeDocument "),
		
		
		/* Recherche de la liste d'images avec metadatas d'un produit logement*/
		
		@NamedQuery(
				name= "rechercherDocumentListParProduitLogementParTypeDocument", 
				query = "SELECT  pld.document FROM ProduitLogementDocument pld "
						+ " WHERE pld.produitLogement.id = :idProduitLogement "
						+ " AND pld.document.typeDocument.id = :idTypeDocument "),
		
		
		
		/* Recherche de la liste de documents appartenant à un produit logement */
		
		@NamedQuery(
				name= "rechercherDocumentListParIdProduitLogement", 
				query = "SELECT  pld.document FROM ProduitLogementDocument pld "
						+ " WHERE pld.produitLogement.id = :idProduitLogement  "),
		
		/* Suppression des informations du produit logement dans la classe d'aasociation */
		
		@NamedQuery(
				name= "suppressionInformationsProduitLogementDocument", 
				query = "DELETE  FROM ProduitLogementDocument pld "
						+ " WHERE pld.produitLogement.id = :idProduitLogement  "),
		
		
		/* Supprimer un document par son id dans la classe d'association ProduitLogementDocument */
		
		@NamedQuery(
				name= "supprimerProduitLogementDocumentParId", 
				query = "DELETE  FROM ProduitLogementDocument pld "
						+ " WHERE pld.document.id = :idDocument  ")
		
	})
@Entity
@Table(name="produitlogementdocument")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ProduitLogementDocument implements IEntityStringkey, Serializable, IEntityMsgVarMap {

	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;
	

	//private E entity ;
	
	@ManyToOne//(cascade = (CascadeType.ALL))
	@NotNull(message="Le produit logement ne doit pas être nul")
	private ProduitLogement produitLogement = null;

	
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
	
	public ProduitLogement getProduitLogement() {
		return produitLogement;
	}

	public void setProduitLogement(ProduitLogement produitLogement) {
		this.produitLogement = produitLogement;
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
		return "ProduitLogementDocument [id=" + id + ", produitLogement=" + produitLogement + ", document=" + document
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
