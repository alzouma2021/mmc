package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;


/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@Entity
@Table(name = "tiers")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Tiers  implements Serializable, IEntityStringkey, IEntityMsgVarMap{
	
	
	private static final long serialVersionUID = 1L;
	
	
    @Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private String id = null ;
	
    
    @NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;
	
	@Lob
	private String description = null ;
	
	@NotNull(message = "L'email ne doit pas être nul")
	private String email ;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

	@Override
	public String toString() {
		return "Tiers [designation=" + designation + ", email=" + email + "]";
	}


	@Override
	public Map<String, String> getMsgVarMap() {
		
	Map<String,String> rtn =  new HashMap<String,String>();
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ModeFinancement.CODE_TRADUCTION_NOM_USUEL) ;
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
   

}
