package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementDoubleTest {

ValeurCaracteristiqueProduitLogementDouble valeurCaracteristiqueProduitLogementDouble ;
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementDouble = new ValeurCaracteristiqueProduitLogementDouble();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		
	
		 
		 valeurCaracteristiqueProduitLogementDouble.setValeur(1000000.0);
		
		//Exection et Veriication 
		assertEquals("1000000.0", valeurCaracteristiqueProduitLogementDouble.to(valeurCaracteristiqueProduitLogementDouble).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullDoubleFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementDouble.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementDouble.to(valeurCaracteristiqueProduitLogementDouble).getValeurTexte());
		
	}
}
