package com.siliconwise.mmc.simulationfinancementimmobilier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.logement.Logement;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.user.User;


/**
 * Cette classe renferme les informations relatives à une simulation de financement immobilier
 * 
 * Identite Domaniale : Designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date de creation 22/03/2022
 *
 *
 */
@Entity
@Table(name = "simulationfinancementimmobilier")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class SimulationFinancementImmobilier implements Serializable, 
										IEntityStringkey, IEntityMsgVarMap{

	
	private static final long serialVersionUID = 1L;

	private static final String CODE_TRADUCTION_NOM_USUEL = "simulaationFinancementImmobilier.nonUsuel";
	
	//Creation des constantes pour les valeurs des codes de messages
	

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doproduitLogement.nomUsuelit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150, unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	
	@Lob
	private String description = null;

	
	//OneToOne simulationFinancementImmobilier vers user
	@ManyToOne
	private User user ;
	
	
	@ManyToOne
	private Logement logement ; 

	
	@Version
	private Integer version ;

	public SimulationFinancementImmobilier() {
		super();
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Logement getLogement() {
		return logement;
	}

	public void setLogement(Logement logement) {
		this.logement = logement;
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
		SimulationFinancementImmobilier other = (SimulationFinancementImmobilier) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		return true;
	}
	
	
	
	@Override
	public String toString() {
		return "SimulationFinancementImmobilier [designation=" + designation + ", description=" + description
				+ ", user=" + user + "]";
	}
	

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, SimulationFinancementImmobilier.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}


}
