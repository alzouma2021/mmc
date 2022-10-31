package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementFloatTest {

ValeurCaracteristiqueProduitLogementFloat valeurCaracteristiqueProduitLogementFloat ;
	
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementFloat = new ValeurCaracteristiqueProduitLogementFloat();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		 
		 valeurCaracteristiqueProduitLogementFloat.setValeur(1000000.0f);
		
		//Exection et Veriication 
		assertEquals("1000000.0", valeurCaracteristiqueProduitLogementFloat.to(valeurCaracteristiqueProduitLogementFloat).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullDoubleFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementFloat.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementFloat.to(valeurCaracteristiqueProduitLogementFloat).getValeurTexte());
		
	}

}
