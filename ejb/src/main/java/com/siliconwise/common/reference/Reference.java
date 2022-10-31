/**
 * 
 */
package com.siliconwise.common.reference;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

@Table(name="reference",
	uniqueConstraints = {
			@UniqueConstraint(columnNames={ "designation" , "famille_id"})
	})
@NamedQueries(value = {
		@NamedQuery(
				name = "referenceParDesignationReference", 
				query = "SELECT r FROM Reference r where r.designation = :designation" ),
		
		@NamedQuery(
				name = "toutesLesReferenceParIdFamille", 
				query = "SELECT r FROM Reference r where r.famille.id = :searchIdFamille" ),
				
})
//@XmlRootElement(name = "Reference")
@Entity @EntityListeners(UUIDGeneratorEntityListener.class)
//public class Reference implements Serializable, IEntityStringkey, IReference, IReferenceTr, Cloneable
public class Reference implements Serializable, IEntityStringkey, IReference, IReferenceTr , Cloneable, IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;
	
	// code de traduction
	//TODO actualiser l'internationalisation
	public final static String CODE_TRADUCTION_NOM_USUEL = "reference.nomUsuel" ;
	
	// TODO A completer les Id de references  au fur et a mesure
	
	//Valeur de reference de la famille : typeValeur
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_INTEGER = "ref.element.typeValeur.integer" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_BOOLEAN = "ref.element.typeValeur.boolean" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_DOUBLE = "ref.element.typeValeur.double" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_DATETIME = "ref.element.typeValeur.datetime" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_DATE = "ref.element.typeValeur.date" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_TIME = "ref.element.typeValeur.time" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_STRING = "ref.element.typeValeur.string" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_TEXTE = "ref.element.typeValeur.texte" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_LONG = "ref.element.typeValeur.long" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_FLOAT	= "ref.element.typeValeur.float" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_REFERENCE	= "ref.element.typeValeur.reference" ;
	public final static String REF_ELEMENT_ID_TYPE_VALEUR_VILLE	= "ref.element.typeValeur.ville" ;
	
	
	public final static String REF_ELEMENT_ID_VALEUR_VILLA	= "ref.element.valeur.villa";
	public final static String REF_ELEMENT_ID_VALEUR_APPARTEMENT	= "ref.element.valeur.appartement";
	public final static String REF_ELEMENT_ID_VALEUR_LOGEMENT	= "ref.element.valeur.logement";
	public final static String REF_ELEMENT_ID_COMPTANT_SUR_SITUATION	= "ref.element.valeur.comptantsursituation";
	public final static String REF_ELEMENT_ID_CREDIT_BANCAIRE	= "ref.element.valeur.creditbancaire";
	public final static String REF_ELEMENT_ID_TEMPERAMENT	= "ref.element.valeur.temperament";
	public final static String REF_ELEMENT_ID_TIERS_COLLECTEUR	= "ref.element.valeur.tierscollecteur";
	public static final String REF_ELEMENT_ID_COMPTANT = "ref.element.valeur.comptant";
	
	public static final String REF_ELEMENT_PRODUIT_LOGEMENT = "ref.element.produitLogement";
	public static final String REF_ELEMENT_PROGRAMME_IMMOBILIER = "ref.element.programmeImmobilier";
	
	public final static String REF_ELEMENT_VALIDER	= "valider";
	public final static String REF_ELEMENT_CREER	= "creer";
	public final static String REF_ELEMENT_ACTIVER	= "activer";
	public final static String REF_ELEMENT_CONNECTER = "connecter";
	public final static String REF_ELEMENT_DECONNECTER = "deconnecter";
	public final static String REF_ELEMENT_DESACTIVER = "desactiver";
	public final static String REF_ELEMENT_MODIFIER = "modifier";
	public final static String REF_ELEMENT_SUPPRIMER = "supprimer";
	
	
	
	
	
	//Type document pour la consultation dans le cadre de la recherche de produit logement
	
	public final static String REF_ELEMENT_ID_TYPE_IMAGE = "ref.element.typeValeur.image";
	public final static String REF_ELEMENT_ID_TYPE_IMAGE_CONSULTATION = "ref.element.typeValeur.imageConsultation";
	public final static String REF_ELEMENT_ID_TYPE_VIDEO = "ref.element.typeValeur.video";
	
	
	//TODO Définir une famille pour type document pour la demande d'acquisation de logement
	
	
	//Type Entity
	
	public final static String REF_ELEMENT_TYPE_PROGRAMMEIMMOBILIER = "ref.element.type.programmeImmobilier";
	public final static String REF_ELEMENT_TYPE_PRODUITLOGEMENT = "ref.element.type.produitLogement";
	
	
	//public static final String REFERENCE_ID_CIVILITE_MLLE = "ref.element.mlle" ;
	

	public Reference() {}
	public Reference(String param) {}
	
	// Id n'est pas un UUID. 
	@Id
	@Column(length = 50 )
	private String id = null ;
	
	@Lob
	private String description = null ;
	
	@Column(length = 150)
	private String code = null ;
	
	@Column(length = 150) 
	private String codeTrDescription = null ;
	
	@Column(length = 150)
	private String codeTrDesignation = null ; 
	
	// @NotNull(message = "")
	// @NotEmpty(message = "")
	@Column(length = 150 )
	// @Size(max= 150, message = "")
	private String designation = null ;
	
	@ManyToOne  //@JoinColumn(name="famille_id")
	private ReferenceFamille famille = null ;
	
	@Version 
	protected Integer version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Transient
	public String getCodeTrDescription() {
		return codeTrDescription;
	}
	public void setCodeTrDescription(String codeTrDescription) {
		this.codeTrDescription = codeTrDescription;
	}

	@Transient
	public String getCodeTrDesignation() {
		return codeTrDesignation;
	}

	public void setCodeTrDesignation(String designationTr) {
		this.codeTrDesignation = designationTr;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String getCode() {
		
		return code ;
	}
	@Override
	public void setCode(String code) {
		this.code =  code ;
	}
	
	/*
	@Override
	@Transient
	public String getLocalizedDesignation() {
		return null;
	}*/

	public ReferenceFamille getFamille() {
		return famille;
	}
	public void setFamille(ReferenceFamille famille) {
		this.famille = famille;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	/*
	@Transient
	public String[] getMessageVarList() {
		
		return new String[]{} ;
	}*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Reference other = (Reference) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reference [id=" + id + ", designation=" + designation + "]";
	}
	
	@Override
	public Object clone() {
		
		Object o = null;
		
		try {
			o = super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace();
		}
		
		// on renvoie le clone
		return o;
	}
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
		

		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Reference.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
	
}
