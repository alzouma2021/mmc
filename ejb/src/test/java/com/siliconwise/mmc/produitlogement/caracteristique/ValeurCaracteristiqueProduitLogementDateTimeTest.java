package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementDateTimeTest {

ValeurCaracteristiqueProduitLogementDateTime valeurCaracteristiqueProduitLogementDateTime ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementDateTime = new ValeurCaracteristiqueProduitLogementDateTime();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		 
		 valeurCaracteristiqueProduitLogementDateTime.setValeur(LocalDateTime.of(2021,12,12,10,10,10 ));
		
		//Exection et Veriication 
		assertEquals("2021-12-12T10:10:10", valeurCaracteristiqueProduitLogementDateTime.to(valeurCaracteristiqueProduitLogementDateTime).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullDateTimeFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementDateTime.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementDateTime.to(valeurCaracteristiqueProduitLogementDateTime).getValeurTexte());
		
	}


}
