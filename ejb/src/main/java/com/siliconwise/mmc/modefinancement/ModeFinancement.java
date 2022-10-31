package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;



/**
 * @author Alzouma Moussa Mahamadou
 *
 */

@NamedEntityGraphs(value={
		
		//Retourne juste les propriétés nécessaires pour la remise en persistence de ladite entité
		
		/**
		 * 
		 * Ce nameGraph retourne un mode de financement avec la propriété id
		 * Utilisation de ce namedGraph pour remettre un mode de financement dans un contexte de persistence
		 *
		 */
		@NamedEntityGraph(
		     name="graph.modeFinancement.id",
			 attributeNodes= {
				  @NamedAttributeNode(value="id")
		     })
		
})


@NamedQueries(value={
		
		/*Retourne l'Id d'un mode de financement en fonction de sa designation et du programme immobilier
		 * auquel  il appartient
		 */		
		
		@NamedQuery(
				name="modeFinancementIdParDesignationParProgramme",
				query="SELECT DISTINCT mf.id FROM ModeFinancement mf "
						+ "WHERE mf.designation = :designation "
						+ "AND   mf.programmeImmobilier.code = :programmeCode" ) ,
		
		/*
		 * Retourne tous les mode de financements 
		 */
		
		@NamedQuery(
				name="modeFinancementsDunProgrammeImmobilierNonValides",
				query="SELECT mf FROM ModeFinancement mf "
						+ "WHERE mf.programmeImmobilier.id = :IdProgramme "
						+ "AND   mf.estValide = false " ) ,
		
		/*
		 * 
		 * Retourne les informations sur les palliers d'un mode financement de type comptant sur situatioon
		 * 
		 */
		
		@NamedQuery(
				name="pallierComptantSurSituationListParModeFinancement",
				query="SELECT pl FROM PallierComptantSurSituation pl , ModeFinancement mf "
						+ "WHERE mf.id = :IdModeFinancement "  ) ,
		
		
		/*
		 * 
		 * rechercher de mode mode financements d'un programme immobilier donné
		 * 
		 */
		
		@NamedQuery(
				name="rechercherModeFinancementListParPogrammeimmobilier",
				query="SELECT mf FROM ModeFinancement mf "
						+ "WHERE mf.programmeImmobilier.id = :idProgrammeImmobilier "  ), 
		
		
		/*
		 * 
		 * rechercher de mode mode financements par efi 
		 * 
		 */
		
		@NamedQuery(
				name="rechercherModeFinancementListParEfi",
				query="SELECT mf FROM ModeFinancement mf "
						+ "WHERE mf.creditBancaire.eFi.id = :idEfi "  ) 
		
})
@Entity
@Table(name = "modefinancement")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ModeFinancement implements Serializable, IEntityStringkey, IEntityMsgVarMap  {
	
	
	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
		public static final String CODE_TRADUCTION_NOM_USUEL = "modeFinancement.nomUsuel" ;

		public static final String CODE_TRADUCTION_MODE_FINANCEMENT_NON_VALIDE = "modeFinancement.non-valide";
		
		public static final String CODE_TRADUCTION_EFI_NON_DEFINIE = "efi.non-definie" ;
	
	
		
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private String  id = null ;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150 , unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;
	
	@Lob
	private String description = null ;
	
	private Boolean estValide = null ;
	
	
	//Relation entre ModeFinancement et PallierComptantSurSituation
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
	private Set<PallierComptantSurSituation> pallierComptantSurSituationList = null;
	
	
	//Relation entre ModeFinancement et CreditBanciare
	
	@OneToOne
	private CreditBancaire creditBancaire = null ;
	
	//Relation entre ModeFinancement et Reference
	
	@NotNull(message = "le typeFinancement ne doit pas être null")
	@OneToOne
	private TypeFinancement typeFinancement ;
	
	
	//Relation ModeFinancement vers Compte
	
	@OneToOne
	private Compte compteSequestre = null ;
	
	
	//Relation ModeFinancement vers ProgrammeImmobilier
	
	//@NotNull(message = "le programmeImmobilier  ne doit pas être null")
	@ManyToOne
	private ProgrammeImmobilier programmeImmobilier  ;
	
	//Relation ModeFinancement vers Temperament

	@OneToOne
	private Temperament temperament = null ;
	
	
	//Relation ModeFinancement vers Tiers collecteur
    
	@OneToOne
	private TiersCollecteur tierscollecteur = null ;
	
	
	@Version
	private Integer version = null ;
	
	
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

	public Set<PallierComptantSurSituation> getPallierComptantSurSituationList() {
		return pallierComptantSurSituationList;
	}
	
	public void setPallierComptantSurSituationList(Set<PallierComptantSurSituation> pallierComptantSurSituationList) {
		this.pallierComptantSurSituationList = pallierComptantSurSituationList;
	}

	public CreditBancaire getCreditBancaire() {
		return creditBancaire;
	}

	public void setCreditBancaire(CreditBancaire creditBancaire) {
		this.creditBancaire = creditBancaire;
	}

	public Compte getCompteSequestre() {
		return compteSequestre;
	}

	public void setCompteSequestre(Compte compteSequestre) {
		this.compteSequestre = compteSequestre;
	}

	public Boolean getEstValide() {
		return estValide;
	}

	public void setEstValide(Boolean estValide) {
		this.estValide = estValide;
	}

	public TypeFinancement getTypeFinancement() {
		return typeFinancement;
	}

	public void setTypeFinancement(TypeFinancement typeFinancement) {
		this.typeFinancement = typeFinancement;
	}

	
	public ProgrammeImmobilier getProgrammeImmobilier() {
		return programmeImmobilier;
	}

	public void setProgrammeImmobilier(ProgrammeImmobilier programmeImmobilier) {
		this.programmeImmobilier = programmeImmobilier;
	}
	
	
	public Temperament getTemperament() {
		return temperament;
	}

	public void setTemperament(Temperament temperament) {
		this.temperament = temperament;
	}

	public TiersCollecteur getTierscollecteur() {
		return tierscollecteur;
	}

	public void setTierscollecteur(TiersCollecteur tierscollecteur) {
		this.tierscollecteur = tierscollecteur;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	

	@Override
	public String toString() {
		return "ModeFinancement [designation=" + designation + ", typeFinancement=" + typeFinancement
				+ ", programmeImmobilier=" + programmeImmobilier + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
	Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ModeFinancement.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
	
	
}
