package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurIntegerTest {
	
	ValeurInteger valeurInteger ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurInteger = new ValeurInteger();
	}
	
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Test
	void testFrom() {
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		ArrayList val = new ArrayList();
		val.add("2");
		val.add("5");
		val.add("2");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Exection et Veriication 
		assertEquals(5, valeurInteger.from(valeur).getValeur().get(1));
	}

	
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	@Test
	void testNotIntegerFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = new Valeur<Object>();
		
		ArrayList val = new ArrayList();
		val.add("nonInteger");
		
		valeur.setValeurTexte(val);
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NumberFormatException.class,()->valeurInteger.from(valeur).getValeur());
	}


	@SuppressWarnings("static-access")
	@Test
	void testNullIntegerFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurInteger.from(valeur).getValeur());
	}
	
}
