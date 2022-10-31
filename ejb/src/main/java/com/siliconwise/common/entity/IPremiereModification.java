/**
 * 
 */
package com.siliconwise.common.entity;

import java.time.ZonedDateTime;



/**
 * Infor mation d'enregistremet.
 * Enregistreent correpond à la première sauvegarde
 * @author TOSHIBA
 *
 */
public interface IPremiereModification {

	public abstract ZonedDateTime getDateHeureEnregistrement() ;

	public abstract void setDateHeureEnregistrement(ZonedDateTime dateHeureEnregistrementPosePrevue) ;

	public abstract String getAuteurIdEnregistrement() ;
	
	public void setAuteurIdEnregistrement(String auteurIdEnregistrementPosePrevue) ;

	public String getAuteurEnregistrement() ;

	public void setAuteurEnregistrement(String auteurEnregistrementPosePrevue) ;

}
