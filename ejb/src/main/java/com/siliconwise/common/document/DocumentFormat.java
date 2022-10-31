package com.siliconwise.common.document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Cette classe contient les formats de document
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 01/02/2021
 *
 */
@NamedQueries(value = {
		
		//Rechercher de tout les formats dispobibles
		
		@NamedQuery(
			name= "allDocumentFormats", 
			query = "SELECT  df FROM DocumentFormat df ORDER BY df.designation ")
		
	})
@Entity @Table(name = "documentformat" )
@EntityListeners(UUIDGeneratorEntityListener.class)
public class DocumentFormat implements IEntityStringkey, Serializable, IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
	public static final String CODE_TRADUCTION_NOM_USUEL = "documentformat.nomUsuel" ;
	
	@Id
	@NotNull(message = "L'Id ne doit pas être nul")
	@NotEmpty(message = "L'Id ne doit pas être vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique=true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation = null;

	@Lob
	private String description = null;
	
	@Column(length = 150 )
	private String typeMime  = null ;
	
	@Column(length = 150 )
	private String extension  = null ;
	
	@Version
	private Integer version ;

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

	public String getTypeMime() {
		return typeMime;
	}

	public void setTypeMime(String typeMime) {
		this.typeMime = typeMime;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	

	@Override
	public String toString() {
		return "DocumentFormat [id=" + id + ", designation=" + designation + ", description=" + description
				+ ", typeMime=" + typeMime + ", extension=" + extension + ", version=" + version + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, DocumentFormat.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
	}
	
}
