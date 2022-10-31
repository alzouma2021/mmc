package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class ValeurCaracteristiqueProduitLogementIntegerTest {

	
	
	ValeurCaracteristiqueProduitLogementInteger valeurCaracteristiqueProduitLogementInteger ;
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementInteger = new ValeurCaracteristiqueProduitLogementInteger();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
	
		 
		 valeurCaracteristiqueProduitLogementInteger.setValeur(100);
		
		//Exection et Veriication 
		assertEquals("100", valeurCaracteristiqueProduitLogementInteger.to(valeurCaracteristiqueProduitLogementInteger).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullIntegerFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementInteger.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementInteger.to(valeurCaracteristiqueProduitLogementInteger).getValeurTexte());
		
	}
	
}
