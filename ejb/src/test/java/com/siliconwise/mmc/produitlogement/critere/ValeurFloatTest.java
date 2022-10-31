package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurFloatTest {
	
	ValeurFloat valeurFloat ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurFloat = new ValeurFloat();
	}
	//Test avec Float du type x.y
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Test
	void testPositiveFloatFrom() {
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		ArrayList val = new ArrayList();
		val.add("15000000f");
		val.add("40000000f");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
				
		//Exection et Veriication
		ArrayList reslt = new ArrayList();
		reslt.add(15000000f);
		reslt.add(40000000f);
		
		assertEquals(reslt ,valeurFloat.from(valeur).getValeur());
	}
	
	
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	@Test
	void testNegativeFloatFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = new Valeur<Object>();
		ArrayList val = new ArrayList();
		val.add("-15000000f");
		val.add("-40000000f");
		
		valeur.setValeurTexte(val);
		
		//Exection et Verification 
		ArrayList reslt = new ArrayList();
		reslt.add(-15000000f);
		reslt.add(-40000000f);
		
		assertEquals(reslt ,valeurFloat.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testMultiFloatFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("-1.213f");
		val.add("-2.489f");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Exection et Verification 
		@SuppressWarnings("rawtypes")
		ArrayList reslt = new ArrayList();
		reslt.add(-1.213f);
		reslt.add(-2.489f);
		
		assertEquals(reslt ,valeurFloat.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNullFloatFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add(null);
		val.add(null);
		
		Valeur<Object> valeur =  new Valeur<Object>();;
		valeur.setValeurTexte(val);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurFloat.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNotFloatFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("nonInteger");
		val.add("nonInteger");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NumberFormatException.class,()->valeurFloat.from(valeur).getValeur());
	}
	
}
