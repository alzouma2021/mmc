package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

/**
 * 
 * Identité Domaniale : designation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
public  class Valeur<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String designation = null;

	private String description = null;

	private Integer version;
	
	
	private T myValeur ; 
	
	//valeurTexte , une variable à utilisation temporaire qui contient la valeur transmise par le FrontEnd
	private  List<String> valeurTexte = null;
	
	
	public Valeur() {
		super();
	}
	
	
	
	public Valeur(T valeur) {
		super();
		setValeur(valeur);
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
	
	public List<String> getValeurTexte() {
		return valeurTexte;
	}

	public void setValeurTexte(List<String> valeurTexte) {
		this.valeurTexte = valeurTexte;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((designation == null) ? 0 : designation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Valeur<T> other = (Valeur<T>) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
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
		return "Valeur [designation=" + designation + "]";
	}

	@SuppressWarnings("unchecked")

	public  List<T> getValeur(){
		return (List<T>) myValeur ; 
	} 
	
	public  void setValeur(T myValeur){
		 this.myValeur = myValeur ;
	}
	
}
