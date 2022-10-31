package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurDoubleTest {

ValeurDouble valeurDouble ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurDouble = new ValeurDouble();
	}
	//Test avec Double du type x.y
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testPositiveDoubleFrom() {
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("42.1564");
		val.add("7894.789");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
				
		//Exection du test et Verification des sortants
		ArrayList<Double> reslt = new ArrayList<Double>();
		reslt.add(42.1564);
		reslt.add(7894.789);
		
		assertEquals(reslt,valeurDouble.from(valeur).getValeur());
	}
	
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNegativeDoubleFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("-42.1564");
		val.add("-7894.789");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Exection et Verification 
		ArrayList<Double> reslt = new ArrayList<Double>();
		reslt.add(-42.1564);
		reslt.add(-7894.789);
		
		assertEquals(reslt,valeurDouble.from(valeur).getValeur());
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNullDoubleFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurDouble.from(valeur).getValeur());
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNotDoubleFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		ArrayList<String> val = new ArrayList<String>();
		val.add("nonDouble");
		val.add("nonDouble");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(IllegalArgumentException.class,()->valeurDouble.from(valeur).getValeur());
	}

}
