package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementTimeTest {

ValeurCaracteristiqueProduitLogementTime valeurCaracteristiqueProduitLogementTime ;

	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementTime = new ValeurCaracteristiqueProduitLogementTime();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		
	
		 
		 valeurCaracteristiqueProduitLogementTime.setValeur(LocalTime.parse("10:10", DateTimeFormatter.ISO_LOCAL_TIME));
		
		//Exection et Veriication 
		assertEquals("10:10", valeurCaracteristiqueProduitLogementTime.to(valeurCaracteristiqueProduitLogementTime).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullTimeFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementTime.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementTime.to(valeurCaracteristiqueProduitLogementTime).getValeurTexte());
		
	}



}
