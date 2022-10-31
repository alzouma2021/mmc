package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurCaracteristiqueProduitLogementBooleanTest {

	ValeurCaracteristiqueProduitLogementBoolean valeurCaracteristiqueProduitLogementBoolean ;
	
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurCaracteristiqueProduitLogementBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		
		//Initialisation des valeurs entrantes
		 
		 valeurCaracteristiqueProduitLogementBoolean.setValeur(true);
		
		//Exection et Veriication 
		assertEquals("true", valeurCaracteristiqueProduitLogementBoolean.to(valeurCaracteristiqueProduitLogementBoolean).getValeurTexte());
		
		
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullBooleanFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		valeurCaracteristiqueProduitLogementBoolean.setValeur(null);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()-> valeurCaracteristiqueProduitLogementBoolean.to(valeurCaracteristiqueProduitLogementBoolean).getValeurTexte());
		
	}
	

}
