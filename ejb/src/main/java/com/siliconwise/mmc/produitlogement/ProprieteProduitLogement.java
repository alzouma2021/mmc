package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.common.reference.ReferenceFamille;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Identtité Domaniale : designation Cette classe renferme les proprietes
 * produit logement
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
@NamedQueries(value={
		@NamedQuery(
				name="trouverToutesLesProprietesProduitLogement",
				query="SELECT ppl FROM ProprieteProduitLogement ppl")
})
@Entity
@Table(name = "proprieteproduitlogement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ProprieteProduitLogement implements Serializable, IEntityStringkey, IEntityMsgVarMap {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull(message = "L'Id ne doit pas être null")
	@NotEmpty(message = "L'Id ne doit pas être vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "La designation ne doit pas être vide")
	@Column(length = 150, unique = true)
	@Size(max = 150, message = "La designation ne doit pas depasser 150 caracteres")
	private String designation = null;

	@NotNull(message = "Le code ne doit pas être nul")
	@NotEmpty(message = "Le code ne doit pas être vide")
	@Column(length = 50, unique = true)
	@Size(max = 50, message = "Le code ne doit pas depasser 50 caracteres")
	private String code = null;
	
	@Lob
	private String description = null;

	// ManyToOne ProprieteProduitLogement vers Reference
	@ManyToOne
	private Reference type = null;

	// ManyTone ProprieteProduitLogement vers FammilleProprieteProduitLogement
	@ManyToOne
	private FamilleProprieteProduitLogement familleProprieteProduitLogement = null;
	
	private boolean estCaracteristiqueProduitLogement  ;
	
	//ManyToOne ProprieteProduitLogement vers Document
	@ManyToOne
	private Document icone = null;

	@Version
	private Integer version;
	
	private Boolean estObligatoire = null;
	
	private Boolean estDansListe = null;
	
	private Boolean estUsuelleFiltre = null;
	
	private Boolean estChoixMultipleFiltre = null;
	
	private Integer minValueIntegerFiltre = null ;
	
	private Integer maxValueIntegerFiltre = null ;
	
	private Float minValueFloatFiltre = null ;
	
	private Float maxValueFloatFiltre = null ;
	
	private Double minValueDoubleFiltre = null ;
	
	private Double maxValueDoubleFiltre = null ;
	
	private Long   minValueLongFiltre = null ;
	
	private Long   maxValueLongFiltre = null ;
	
	private LocalDate minValueDateFilter = null ;
	
	private LocalDate maxValueDateFilter = null ;
	
	private LocalDateTime minValueDateTimeFilter = null ;
	
	private LocalDateTime maxValueDateTimeFilter = null ;
	
	
	@ManyToOne
	private ReferenceFamille familleSiTypeReference = null ;

	public ProprieteProduitLogement() {
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

	public Reference getType() {
		return type;
	}

	public void setType(Reference type) {
		this.type = type;
	}

	
	public FamilleProprieteProduitLogement getFamilleProprieteProduitLogement() {
		return familleProprieteProduitLogement;
	}

	public void setFamilleProprieteProduitLogement(FamilleProprieteProduitLogement familleProprieteProduitLogement) {
		this.familleProprieteProduitLogement = familleProprieteProduitLogement;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Document getIcone() {
		return icone;
	}

	public void setIcone(Document icone) {
		this.icone = icone;
	}
	
	public Boolean getEstObligatoire() {
		return estObligatoire;
	}

	public void setEstObligatoire(Boolean estObligatoire) {
		this.estObligatoire = estObligatoire;
	}

	public Boolean getEstDansListe() {
		return estDansListe;
	}

	public void setEstDansListe(Boolean estDansListe) {
		this.estDansListe = estDansListe;
	}
	
	public Boolean getEstUsuelleFiltre() {
		return estUsuelleFiltre;
	}

	public void setEstUsuelleFiltre(Boolean estUsuelleFiltre) {
		this.estUsuelleFiltre = estUsuelleFiltre;
	}

	public Boolean getEstChoixMultipleFiltre() {
		return estChoixMultipleFiltre;
	}

	public void setEstChoixMultipleFiltre(Boolean estChoixMultipleFiltre) {
		this.estChoixMultipleFiltre = estChoixMultipleFiltre;
	}

	public Integer getMinValueIntegerFiltre() {
		return minValueIntegerFiltre;
	}

	public void setMinValueIntegerFiltre(Integer minValueIntegerFiltre) {
		this.minValueIntegerFiltre = minValueIntegerFiltre;
	}

	public Integer getMaxValueIntegerFiltre() {
		return maxValueIntegerFiltre;
	}

	public void setMaxValueIntegerFiltre(Integer maxValueIntegerFiltre) {
		this.maxValueIntegerFiltre = maxValueIntegerFiltre;
	}

	public Float getMinValueFloatFiltre() {
		return minValueFloatFiltre;
	}

	public void setMinValueFloatFiltre(Float minValueFloatFiltre) {
		this.minValueFloatFiltre = minValueFloatFiltre;
	}

	public Float getMaxValueFloatFiltre() {
		return maxValueFloatFiltre;
	}

	public void setMaxValueFloatFiltre(Float maxValueFloatFiltre) {
		this.maxValueFloatFiltre = maxValueFloatFiltre;
	}

	public Double getMinValueDoubleFiltre() {
		return minValueDoubleFiltre;
	}

	public void setMinValueDoubleFiltre(Double minValueDoubleFiltre) {
		this.minValueDoubleFiltre = minValueDoubleFiltre;
	}

	public Double getMaxValueDoubleFiltre() {
		return maxValueDoubleFiltre;
	}

	public void setMaxValueDoubleFiltre(Double maxValueDoubleFiltre) {
		this.maxValueDoubleFiltre = maxValueDoubleFiltre;
	}

	public Long getMaxValueLongFiltre() {
		return maxValueLongFiltre;
	}

	public void setMaxValueLongFiltre(Long maxValueLongFiltre) {
		this.maxValueLongFiltre = maxValueLongFiltre;
	}

	public LocalDate getMinValueDateFilter() {
		return minValueDateFilter;
	}

	public void setMinValueDateFilter(LocalDate minValueDateFilter) {
		this.minValueDateFilter = minValueDateFilter;
	}

	public LocalDate getMaxValueDateFilter() {
		return maxValueDateFilter;
	}

	public void setMaxValueDateFilter(LocalDate maxValueDateFilter) {
		this.maxValueDateFilter = maxValueDateFilter;
	}

	public LocalDateTime getMinValueDateTimeFilter() {
		return minValueDateTimeFilter;
	}

	public void setMinValueDateTimeFilter(LocalDateTime minValueDateTimeFilter) {
		this.minValueDateTimeFilter = minValueDateTimeFilter;
	}

	public LocalDateTime getMaxValueDateTimeFilter() {
		return maxValueDateTimeFilter;
	}

	public void setMaxValueDateTimeFilter(LocalDateTime maxValueDateTimeFilter) {
		this.maxValueDateTimeFilter = maxValueDateTimeFilter;
	}

	public ReferenceFamille getFamilleSiTypeReference() {
		return familleSiTypeReference;
	}

	public void setFamilleSiTypeReference(ReferenceFamille familleSiTypeReference) {
		this.familleSiTypeReference = familleSiTypeReference;
	}
	
	public Long getMinValueLongFiltre() {
		return minValueLongFiltre;
	}

	public void setMinValueLongFiltre(Long minValueLongFiltre) {
		this.minValueLongFiltre = minValueLongFiltre;
	}
	
	public boolean isEstCaracteristiqueProduitLogement() {
		return estCaracteristiqueProduitLogement;
	}

	public void setEstCaracteristiqueProduitLogement(boolean estCaracteristiqueProduitLogement) {
		this.estCaracteristiqueProduitLogement = estCaracteristiqueProduitLogement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProprieteProduitLogement other = (ProprieteProduitLogement) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProprieteProduitLogement [id=" + id + "]";
	}


	
	@Override
	public Map<String, String> getMsgVarMap() {
		
        Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	

}
