package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
import com.siliconwise.mmc.simulationfinancementimmobilier.SimulationFinancementImmobilier;

/**
 * Super classe financement
 * 
 * Identité domaniale : designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 08/01/2021
 * 
 */
@NamedQueries(value={
		
		/*Retourne l'id d'une caracteristique en fonction de la designation et du produit logement*/
	
		@NamedQuery(
				name="financementIdParDesignation",
				query="SELECT DISTINCT fi.id FROM Financement fi "
						+ "WHERE fi.designation = :designation ") ,
		
})
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="financement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Financement implements Serializable , IEntityStringkey , IEntityMsgVarMap{


	private static final long serialVersionUID = 1L;


	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille max de l'Id est de 50")
	private String id;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;
	
	private Double montant ;
	
	//date et heure de création de l'instance de Financement qui correspond à la date et heure de l'exécution de la simulation
	private LocalDate DateEtHeure ;
	
	@ManyToOne
	private SimulationFinancementImmobilier	simulationFinancementImmobilier ; 
	
	
	@Version
	private Integer version;

	public Financement() {
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public LocalDate getDateEtHeure() {
		return DateEtHeure;
	}

	public void setDateEtHeure(LocalDate dateEtHeure) {
		DateEtHeure = dateEtHeure;
	}

	public SimulationFinancementImmobilier getSimulationFinancementImmobilier() {
		return simulationFinancementImmobilier;
	}

	public void setSimulationFinancementImmobilier(SimulationFinancementImmobilier simulationFinancementImmobilier) {
		this.simulationFinancementImmobilier = simulationFinancementImmobilier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, "Caracteristique du logement");
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDescription());
		
		return rtn;
	}
	

}
