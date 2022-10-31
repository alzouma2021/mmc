package com.siliconwise.common.document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Cette classe renferme les informations relatives aux document
 * 
 * @author	Alzouma Moussa Mahamadou
 * @date	01/02/2021	
 *
 */
@NamedQueries(value = {
		
		/* Recherche de document par path  */
		
		@NamedQuery(
			name= "documentIdParPath", 
			query = "SELECT  d.id FROM Document d WHERE d.path = :path"),
		
		/* Recherche de document par designation*/
		
		@NamedQuery(
				name= "documentIdParDesignation", 
				query = "SELECT  d.id FROM Document d WHERE d.designation = :designation"),
		
		/* Recherche de document par Id  */
		
		@NamedQuery(
			name= "documentParId", 
			query = "SELECT  d FROM Document d WHERE d.id = :idDocument"),
		
	})

@NamedEntityGraphs(value={
		
		//Minimun sans le contenu 
		
		/**
		 * 
		 * Ce nameGraph nous retournera juste les proprietes minimales sans le contenu  du document
		 *
		 */
		@NamedEntityGraph(
		     name="graph.document.minimum-sans-contenu",
			 attributeNodes= {
				  @NamedAttributeNode(value="id"),
				  @NamedAttributeNode(value="designation"),
				  @NamedAttributeNode(value="description"),
				  @NamedAttributeNode(value="path"),
				  @NamedAttributeNode(value="format", subgraph="documentFormat"),
				  @NamedAttributeNode(value="typeDocument", subgraph="reference"),
				  @NamedAttributeNode(value="version")	 
			 },
			 subgraphs = {					
					 @NamedSubgraph(
							 name="documentFormat",
							 attributeNodes={
									@NamedAttributeNode(value="id"),
									@NamedAttributeNode(value="designation"),
									@NamedAttributeNode(value="typeMime"),
									@NamedAttributeNode(value="extension")
					 }),
					 
					@NamedSubgraph(
						     name="reference", 
							 attributeNodes={
									@NamedAttributeNode(value="id"),
									@NamedAttributeNode(value="designation"),
									@NamedAttributeNode(value="version")
					})
					 
		}),
		
		
		/**
		 * 
		 * Ce nameGraph nous retournera juste les proprietes minimales avec le contenu du document
		 *
		 */
		@NamedEntityGraph(
				name="graph.document.minimum-avec-contenu", 
				attributeNodes={
					@NamedAttributeNode(value="id"),
					@NamedAttributeNode(value="designation"),
					@NamedAttributeNode(value="description"),
					@NamedAttributeNode(value="path"),
					@NamedAttributeNode(value="format", subgraph="documentFormat"),
					@NamedAttributeNode(value="typeDocument", subgraph="reference"),
					@NamedAttributeNode(value="version")
				},
				subgraphs = {					
					@NamedSubgraph(
						name="documentFormat",
						attributeNodes={
							@NamedAttributeNode(value="id"),
							@NamedAttributeNode(value="designation"),
							@NamedAttributeNode(value="typeMime"),
							@NamedAttributeNode(value="extension")
						}),
					@NamedSubgraph(
							name="reference", 
							attributeNodes={
								@NamedAttributeNode(value="id"),
								@NamedAttributeNode(value="code"),
								@NamedAttributeNode(value="description"),
								@NamedAttributeNode(value="designation"),
								@NamedAttributeNode(value="codeTrDescription"),
								@NamedAttributeNode(value="codeTrDesignation"),
								@NamedAttributeNode(value="version")
						})
				})

		
})
@Entity
@Table(name="document")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Document implements IEntityStringkey, Serializable, IEntityMsgVarMap {

	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
	public static final String CODE_TRADUCTION_NOM_USUEL = "document.nomUsuel" ;
	
	// TODO Actualiser et deplace eventuellement dans AppMessageKeys
	/*
	public static final String CODE_TRADUCTION_NOT_DEFINED = "" ;
	public static final String CODE_TRADUCTION_EXISTE = "" ;
	public static final String CODE_TRADUCTION_PERSISTENCE_INTIGRITY_ERROR = "" ;
	public static final String CODE_TRADUCTION_PERISTENCE_ERREUR = "" ;
	public static final String CODE_TRADUCTION_DUPLICATED_NEW_ENTITY = "" ;
	public static final String CODE_TRADUCTION_DUPLICATE_WITH_DIFFERENT_ID = "" ;
	public static final String CODE_TRADUCTION_NOT_FOUND = "" ;
	public static final String CODE_TRADUCTION_VERSION_NOT_DEFINED= "" ;
	public static final String CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE = "" ;
	public static final String CODE_TRADUCTION_FORMAT_NOT_FOUND = "" ;
	public static final String CODE_TRADUCTION_LINKED_REFERENCE_ATTACHMENT_ERROR = "" ;
	public static final String CODE_TRADUCTION_BEFORE_SAVING_ERREUR = "" ;
	public static final String CODE_TRADUCTION_FORMAT_NON_DEFINI = "" ;
	public static final String CODE_TRADUCTION_DOCUMENT_CREATION_ERROR = "" ;
	*/
	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id = null;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique=true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;
	
	@Transient
	private String contenu = null ;
	

	@Column(length = 250, unique = true)
	private String path  ;
	
	// Association
	
	@ManyToOne
	@NotNull(message="entite.document.format.not_null")
	private DocumentFormat format ;
	
	@ManyToOne
	@NotNull(message="entite.document.typeDocument.not_null")
	private Reference  typeDocument ;
	
	@Version
	private Integer version = null ;
	

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
	
	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public DocumentFormat getFormat() {
		return format;
	}

	public void setFormat(DocumentFormat format) {
		this.format = format;
	}
	
	public Reference getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(Reference typeDocument) {
		this.typeDocument = typeDocument;
	}

	

	@Override
	public String toString() {
		return "Document [id=" + id + ", designation=" + designation + ", description=" + description + ", contenu="
				+ contenu + ", path=" + path + ", format=" + format + ", typeDocument=" + typeDocument + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Document.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
	}
	
}
