package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementLongTest {

	
	ValeurCaracteristiqueProduitLogementLong valeurCaracteristiqueProduitLogementLong ;
	
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementLong = new ValeurCaracteristiqueProduitLogementLong();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes

		 
		 valeurCaracteristiqueProduitLogementLong.setValeur(100000000000L);
		
		//Exection et Veriication 
		assertEquals("100000000000", valeurCaracteristiqueProduitLogementLong.to(valeurCaracteristiqueProduitLogementLong).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullLongFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementLong.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementLong.to(valeurCaracteristiqueProduitLogementLong).getValeurTexte());
		
	}
}
