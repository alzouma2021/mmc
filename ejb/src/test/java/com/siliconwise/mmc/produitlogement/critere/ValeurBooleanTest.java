package com.siliconwise.mmc.produitlogement.critere;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurBooleanTest {

	ValeurBoolean valeurBoolean ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurBoolean
		valeurBoolean = new ValeurBoolean();
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void test() {
		//fail("Not yet implemented");

				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("true");
		val.add("true");
		val.add("false");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
				
		//Exection et Verification 
		@SuppressWarnings("rawtypes")
		ArrayList reslt = new ArrayList();
		reslt.add(true);
		reslt.add(true);
		reslt.add(false);
		
		assertEquals(reslt , valeurBoolean.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNullBooleanFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurBoolean.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNoneBooleanFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = new Valeur<Object>();
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("1");
		val.add("2");
		val.add("3");
		
		valeur.setValeurTexte(val);
		
		//Execution et Verification de l'envoi d'une exception
		@SuppressWarnings("rawtypes")
		ArrayList reslt = new ArrayList();
		reslt.add(false);
		reslt.add(false);
		reslt.add(false);
		
		assertEquals( reslt, valeurBoolean.from(valeur).getValeur());
	}


}
