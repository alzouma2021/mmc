package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
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
@Table(name = "palliercomptantsursituation")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class PallierComptantSurSituation  implements Serializable, IEntityStringkey, IEntityMsgVarMap{
	
	
	private static final long serialVersionUID = 1L;
	
	
    @Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private String id = null ;
	
    @NotNull(message = "le pallier ne doit pas être nul")
    @OneToOne
	private Reference pallier =  null ;
	
    //Compris entre 1 à 100
	@NotNull(message = "le taux  ne doit pas être nulle")
	private Double taux ;
	
	
	//Taux minimum d'acompte 
	@NotNull(message = "le taux  ne doit pas être nulle")
	private Double tauxACompteMini ;
		
	
	@Version
	private Integer version ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Reference getPallier() {
		return pallier;
	}

	public void setPallier(Reference pallier) {
		this.pallier = pallier;
	}

	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double valeur) {
		this.taux = valeur;
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
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ModeFinancement.CODE_TRADUCTION_NOM_USUEL) ;
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
   
	
}
