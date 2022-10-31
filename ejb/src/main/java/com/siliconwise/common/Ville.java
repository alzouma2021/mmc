package com.siliconwise.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Cette classe renferme les informations sur la ville
 * 
 * @author  Alzouma Moussa Mahamadou
 * @date 	11/01/2021
 * 
 */
@NamedQueries(value={
		
		@NamedQuery(
				name= "toutesLesVilles", 
				query = "SELECT vi FROM Ville vi")
		
})
@Entity
@Table(name="ville")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Ville implements Serializable,IEntityStringkey, IEntityMsgVarMap{
	
	   private static final long serialVersionUID = 1L;
	   
	 //Creation des constantes pour les valeurs des codes de messages
		
	   public static final String CODE_TRADUCTION_NOM_USUEL = "ville.nomUsuel" ;

	   @Id
	   @NotNull(message="L'Id ne doit pas être nul")
	   @NotEmpty(message="L'Id ne doit pas être vide ")
	   @Column(length=50)
	   @Size(max = 50 , message = "La Taille de l'Ide ne doit pas depasser 50 caratères")
	   private String id;
	   
	   @NotNull(message="La designation ne doit pas être nulle")
	   @NotEmpty(message="La designation ne doit pas être vide")
	   @Column(length=150 , unique =  true)
	   @Size(max = 150 , message = "La taille max de la designation est de 150 caratères")
	   private String designation = null;
	  
	   @Lob
	   private String description = null;
	   
	   //ManyToOne Ville Vers Pays
	   @ManyToOne
	   private Pays pays = null ;
	   
	   @Version
	   private Integer version = null ;
	   
	public Ville() {
		
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


	public Pays getPays() {
		return pays;
	}

	public void setPays(Pays pays) {
		this.pays = pays;
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
		Ville other = (Ville) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		return true;
	}
	

	@Override
	public Map<String, String> getMsgVarMap() {
	
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Ville.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
}
