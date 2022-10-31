package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementStringTest {


ValeurCaracteristiqueProduitLogementString valeurCaracteristiqueProduitLogementString ;
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementString = new ValeurCaracteristiqueProduitLogementString();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
	
		 
		 valeurCaracteristiqueProduitLogementString.setValeur("Alzouma");
		
		//Exection et Veriication 
		assertEquals("Alzouma", valeurCaracteristiqueProduitLogementString.to(valeurCaracteristiqueProduitLogementString).getValeurTexte());
		
		
	}
	
	
	
	@SuppressWarnings("static-access")
	@Test
	void testNullStringFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementString.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementString.to(valeurCaracteristiqueProduitLogementString).getValeurTexte());
		
	}


}
