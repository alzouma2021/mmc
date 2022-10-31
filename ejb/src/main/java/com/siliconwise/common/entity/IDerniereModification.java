/**
 * 
 */
package com.siliconwise.common.entity;

import java.time.ZonedDateTime;



/**
 * Suivi de modification assure la non repudiation
 * @author GNAKALE Bernardin
 *
 */
public interface IDerniereModification {

	public abstract String getId();

	public String getAuteurIdDerniereModification() ;
	public void setAuteurIdDerniereModification(String auteur) ;

	public String getAuteurDerniereModification() ;
	public void setAuteurDerniereModification(String auteur) ;
	
	public ZonedDateTime getDateHeureDerniereModification() ;
	public void setDateHeureDerniereModification(ZonedDateTime dateHeure) ;
	
}
