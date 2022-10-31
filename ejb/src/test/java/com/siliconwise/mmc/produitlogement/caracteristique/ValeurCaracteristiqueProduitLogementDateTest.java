package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementDateTest {

ValeurCaracteristiqueProduitLogementDate valeurCaracteristiqueProduitLogementDate ;
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementDate = new ValeurCaracteristiqueProduitLogementDate();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		 
		 valeurCaracteristiqueProduitLogementDate.setValeur(LocalDate.parse("2021-12-12"));
		
		//Exection et Veriication 
		assertEquals("2021-12-12", valeurCaracteristiqueProduitLogementDate.to(valeurCaracteristiqueProduitLogementDate).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullDateFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementDate.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementDate.to(valeurCaracteristiqueProduitLogementDate).getValeurTexte());
		
	}

}
