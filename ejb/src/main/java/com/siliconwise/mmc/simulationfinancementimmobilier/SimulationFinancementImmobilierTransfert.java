package com.siliconwise.mmc.simulationfinancementimmobilier;

import java.util.List;
import java.util.Set;

import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperament;
import com.siliconwise.mmc.demandereservationlogement.financement.Financement;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTransfert;
import com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement.ValeurCaracteristiqueDemandeReservationLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;

/**
 * Classe de transfert contient:
 * 
 * -	Simulation financement immobilier
 * -	ValeurCaracteristiqueDemandeReservationLogement
 * -	Financement
 * 
 * @author sysadmin
 *
 */
public class SimulationFinancementImmobilierTransfert {
	
	
	private SimulationFinancementImmobilier simulationFinancementImmobilier ;
	
	private List<ValeurCaracteristiqueDemandeReservationLogement> valeurCaracteristiqueDemandeReservationLogementList ;

	private FinancementTransfert financementTransfert ;
	
	
	public SimulationFinancementImmobilier getSimulationFinancementImmobilier() {
		return simulationFinancementImmobilier;
	}

	public void setSimulationFinancementImmobilier(SimulationFinancementImmobilier simulationFinancementImmobilier) {
		this.simulationFinancementImmobilier = simulationFinancementImmobilier;
	}

	public List<ValeurCaracteristiqueDemandeReservationLogement> getValeurCaracteristiqueDemandeReservationLogementList() {
		return valeurCaracteristiqueDemandeReservationLogementList;
	}

	public void setValeurCaracteristiqueDemandeReservationLogementList(
			List<ValeurCaracteristiqueDemandeReservationLogement> valeurCaracteristiqueDemandeReservationLogementList) {
		this.valeurCaracteristiqueDemandeReservationLogementList = valeurCaracteristiqueDemandeReservationLogementList;
	}

	public FinancementTransfert getFinancementTransfert() {
		return financementTransfert;
	}

	public void setFinancementTransfert(FinancementTransfert financementTransfert) {
		this.financementTransfert = financementTransfert;
	}

	
	
}
