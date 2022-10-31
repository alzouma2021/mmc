package com.siliconwise.mmc.produitlogement.critere;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.produitlogement.OperateurCritere;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogement;

/**
 * Cette classe renferme les critères de recherche d'un produit logement
 * 
 * Identite Domaniale : designation
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 */
public class CritereRechercheProduitLogement implements Serializable, IEntityMsgVarMap{
	
	   private static final long serialVersionUID = 1L;
	   
	   
	   private String id ;
	   private String designation = null;
	   private String description = null;
	   private ProprieteProduitLogement  proprieteProduitLogement = null ;
	   private OperateurCritere operateurCritere = null ;
	   
	   //Choix du type de la classe Valeur
	   private Valeur<Object> valeurCritere = null ;
	   

		public CritereRechercheProduitLogement() {
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
	
		public ProprieteProduitLogement getProprieteProduitLogement() {
			return proprieteProduitLogement;
		}
	
		public void setProprieteProduitLogement(ProprieteProduitLogement proprieteProduitLogement) {
			this.proprieteProduitLogement = proprieteProduitLogement;
		}
	
		public OperateurCritere getOperateurCritere() {
			return operateurCritere;
		}
	
		public void setOperateurCritere(OperateurCritere operateurCritere) {
			this.operateurCritere = operateurCritere;
		}
	
		public Valeur<Object> getValeurCritere() {
			return valeurCritere;
		}
	
		public void setValeurCritere(Valeur<Object> valeur) {
			this.valeurCritere = valeur;
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
			CritereRechercheProduitLogement other = (CritereRechercheProduitLogement) obj;
			if (designation == null) {
				if (other.designation != null)
					return false;
			} else if (!designation.equals(other.designation))
				return false;
			return true;
		}
	
		@Override
		public String toString() {
			return "CritereRechercheProduitLogement [designation=" + designation + "]";
		}

		
		@Override
		public Map<String, String> getMsgVarMap() {
			
			Map<String,String> rtn =  new HashMap<String,String>();
			
			return rtn;
		}
		
}
