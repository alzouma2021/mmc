package com.siliconwise.mmc.produitlogement.critere;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ValeurLongTest {
	
	ValeurLong valeurLong ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurLong = new ValeurLong();
	}
	
	//Test avec Long du type x.y
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testPositiveLongFrom() {
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("15000000");
		val.add("40000000");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
				
		//Exection et Veriication 
		@SuppressWarnings("rawtypes")
		ArrayList<Long> reslt = new ArrayList<Long>();
		reslt.add(15000000L);
		reslt.add(40000000L);
		
		assertEquals(reslt,valeurLong.from(valeur).getValeur());
	}
	
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNegativeLongFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("-15000000");
		val.add("-40000000");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Exection et Verification 
		@SuppressWarnings("rawtypes")
		ArrayList<Long> reslt = new ArrayList<Long>();
		reslt.add(-15000000L);
		reslt.add(-40000000L);
		
		assertEquals(reslt ,valeurLong.from(valeur).getValeur());
		
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNullLongFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurLong.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNotLongFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList val = new ArrayList();
		val.add("pasunLong");
		val.add("pasunLong");
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(val);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NumberFormatException.class,()->valeurLong.from(valeur).getValeur());
	}

}
