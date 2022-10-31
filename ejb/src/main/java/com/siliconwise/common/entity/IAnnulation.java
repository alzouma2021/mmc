package com.siliconwise.common.entity;

import java.time.ZonedDateTime;

public interface IAnnulation {

	public abstract String getId();
	
	public abstract Boolean getEstAnnule();
	public abstract void setEstAnnule(Boolean estAnnule);

	public abstract String getRaisonAnnulation();
	public abstract void setRaisonAnnulation(String raisonAnnulatio);

	public abstract String getAuteurAnnulation();
	public abstract void setAuteurAnnulation(String auteurAnnulation);
	
	public abstract ZonedDateTime getDateHeureAnnulation();
	public abstract void setDateHeureAnnulation(ZonedDateTime dateHeureAnnulation); 

}