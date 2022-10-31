package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Cette classe renferme les informations relatives aux echeances d'un financement temperament
 * @author Alzouma Moussa Mahamadou
 * @date 24/03/2022
 *
 */
@NamedQueries(value={
		
		/*Retourne l'id d'une caracteristique en fonction de la designation et du produit logement*/
	
		@NamedQuery(
				name="echeancefinancementtemperamentIdParDesignation",
				query="SELECT DISTINCT efit.id FROM EcheanceFinancementTemperament efit "
						+ "WHERE efit.designation = :designation ") ,
		
})
@Entity
@Table(name="echeancefinancementtemperament")
public class EcheanceFinancementTemperament implements Serializable ,IEntityStringkey, IEntityMsgVarMap {
	
	
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

	//Numéro de la periode de l'echeance
	@Column(nullable=false)
	private Integer numeroPeriode ;
	
	private Double montant ;
	
	//EchanceFinancementTemperament vers FinancementTemperament
	
	@ManyToOne
	FinancementTemperament financementTemparement ;
	
	@Version
	private Integer version;
	
	
	public EcheanceFinancementTemperament() {
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

	public Integer getNumeroPeriode() {
		return numeroPeriode;
	}

	public void setNumeroPeriode(Integer numeroPeriode) {
		this.numeroPeriode = numeroPeriode;
	}
	
	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}
	
	public FinancementTemperament getFinancementTemparement() {
		return financementTemparement;
	}

	public void setFinancementTemparement(FinancementTemperament financementTemparement) {
		this.financementTemparement = financementTemparement;
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
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	
	
	
}
