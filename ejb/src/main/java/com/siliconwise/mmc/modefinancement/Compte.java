package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@Entity
@Table(name = "compte")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Compte implements Serializable, IEntityStringkey {
	

	private static final long serialVersionUID = 1L;

	@NotNull(message = "L'Id ne doit pas être nul")
	@NotEmpty(message = "L'Id ne doit pas être vide")
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private String id = null ;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation = null ;
	
	@Lob
	private String description = null ;
	
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
