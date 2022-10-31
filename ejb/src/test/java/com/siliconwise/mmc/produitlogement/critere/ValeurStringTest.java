package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurStringTest {
	
	ValeurString valeurString ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurString
		valeurString = new ValeurString();
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		ArrayList<String> val = new ArrayList<String>();
		 val.add("test1");
		 val.add("test2");
		 val.add("test3");
		 
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
				
		//Exection et Veriication
			ArrayList<String> rslt = new ArrayList<String>();
			 rslt.add("test1");
			 rslt.add("test2");
			 rslt.add("test3");
			 
		assertEquals(rslt , valeurString.from(valeur).getValeur());
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNullStringFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurString.from(valeur).getValeur());
	}
	
}
