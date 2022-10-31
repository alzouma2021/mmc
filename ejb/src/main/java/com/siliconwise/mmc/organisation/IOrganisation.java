package com.siliconwise.mmc.organisation;


/**
 * 
 * Cette interface comporte les informations communes des organisations
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface IOrganisation {
	
	

	public abstract String getId();
	public abstract void setId(String id)  ;

	public abstract String getDescription();
	public abstract void setDescription(String description);

	public abstract String getDesignation();
	public abstract void setDesignation(String designation);
	
	public abstract String getEmail() ;
	public abstract void setEmail(String email) ;
	
	public abstract String getIdentifiantLegal() ;
	public abstract void setIdentifiantLegal(String identifiantLegal) ;
	
	public abstract String getSigle() ;
	public abstract void setSigle(String sigle) ;
	
	public abstract String getTel() ;
	public abstract void setTel(String tel) ;
	
	public abstract String getAdresse() ;
	public abstract void setAdresse(String adresse) ;
	
	public abstract Boolean getEstActive() ;
	public abstract void setEstActive(Boolean estActive) ;
	
	public abstract String getEmailAdmin() ;
	public abstract void setEmailAdmin(String emailAdmin) ;

	public abstract String getNomAdmin() ;
	public abstract void setNomAdmin(String nomAdmin) ;
	
	public abstract String getPrenomAdmin() ;
	public abstract void setPrenomAdmin(String prenomAdmin) ;
	
	

}
