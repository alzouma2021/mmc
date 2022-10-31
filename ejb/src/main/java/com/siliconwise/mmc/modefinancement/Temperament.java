package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
//TODO Mis à jour des propriétés de classe
@Entity
@Table(name = "temperament")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Temperament implements Serializable, IEntityStringkey, IEntityMsgVarMap{
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private  String id = null ;

	@NotNull(message = "L'apport personnel ne doit pas être  nul")
	private Double apportPersonnel ;
	
	@NotNull(message = "La periodicite ne doit pas être  nulle")
	@ManyToOne
	private Reference periodicite ;
	
	@NotNull(message = "Le nombre de periode ne doit pas être  nul")
	//nombre de période maximum accepté par le promoteur
	private Integer  nombrePeriodeMaxi ;
	
	//Date de debut de l'echancier
	@NotNull(message = "la date debut ne doit pas être nulle")
	private LocalDate dateDebut ;
	
	//Relance avant la date butoire de paiment d'un echancier.
	private Integer delaiRelance ;

	//taux minimum d'acompte
	private Double tauxAcompteMini ;
	
	@Version
	private Integer version ;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getApportPersonnel() {
		return apportPersonnel;
	}

	public void setApportPersonnel(Double apportPersonnel) {
		this.apportPersonnel = apportPersonnel;
	}

	public Reference getPeriodicite() {
		return periodicite;
	}

	public void setPeriodicite(Reference periodicite) {
		this.periodicite = periodicite;
	}

	public Integer getNombrePeriodeMaxi() {
		return nombrePeriodeMaxi;
	}

	public void setNombrePeriodeMaxi(Integer nombrePeriode) {
		this.nombrePeriodeMaxi = nombrePeriode;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Integer getDelaiRelance() {
		return delaiRelance;
	}

	public void setDelaiRelance(Integer delaiRelance) {
		this.delaiRelance = delaiRelance;
	}
	
	public Double getTauxAcompteMini() {
		return tauxAcompteMini;
	}

	public void setTauxAcompteMini(Double tauxAcompteMini) {
		this.tauxAcompteMini = tauxAcompteMini;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	@Override
	public String toString() {
		return "Temperament [id=" + id + ", apportPersonnel=" + apportPersonnel + ", periodicite=" + periodicite
				+ ", nombrePeriode=" + nombrePeriodeMaxi + ", dateDebut=" + dateDebut + ", delaiRelance=" + delaiRelance
				+ ", version=" + version + "]";
	}
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
	Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ModeFinancement.CODE_TRADUCTION_NOM_USUEL) ;
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}

}
