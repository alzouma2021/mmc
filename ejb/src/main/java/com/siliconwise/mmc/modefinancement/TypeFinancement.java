package com.siliconwise.mmc.modefinancement;

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

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.organisation.efi.EFi;

@Entity
@Table(name = "typefinancement")
@NamedQueries(value = {
		
		@NamedQuery(
				name = "tousLesTypeFinancements", 
				query = "SELECT tf FROM TypeFinancement tf ORDER BY tf.designation" )
		
})
@EntityListeners(UUIDGeneratorEntityListener.class)
public class TypeFinancement implements Serializable, 
										IEntityStringkey, IEntityMsgVarMap{


	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doproduitLogement.nomUsuelit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@Column(length = 150)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;
	
	
	@NotNull(message = "Le code ne doit pas être nul")
	@Column(name = "code", length = 150 ,unique=true)
	@Size(max = 150, message = "La taille du code ne doit pas depasser 50 caratères")
	private String code ;
	
	
	@NotNull(message = "estvalideParEfi ne doit pas être nul")
	@NotEmpty(message = "estvalideParEfine doit pas être vide")
	private boolean estvalideParEfi = false ;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isEstvalideParEfi() {
		return estvalideParEfi;
	}

	public void setEstvalideParEfi(boolean estvalideParEfi) {
		this.estvalideParEfi = estvalideParEfi;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
	    Map<String,String> rtn =  new HashMap<String,String>();
		
		return rtn;
		
	}

}
