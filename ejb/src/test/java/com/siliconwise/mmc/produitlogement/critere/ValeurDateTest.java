package com.siliconwise.mmc.produitlogement.critere;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurDateTest {

	ValeurDate valeurDate ;
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurDate
		valeurDate = new ValeurDate();
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void test() {
		//fail("Not yet implemented");
		//instanciation de la classe ValeurDate
		@SuppressWarnings("rawtypes")
		ArrayList date = new ArrayList();
		date.add("2021-12-12") ;
		date.add("2020-12-12") ;
		date.add("2018-12-12") ;
		
	    ValeurDate  valeurDate = new ValeurDate();
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(date);
				
		//Exection et Veriication 
		  // resultat
			ArrayList<LocalDate> rslt = new ArrayList<LocalDate>();
			rslt.add(LocalDate.parse("2021-12-12")) ;
			rslt.add(LocalDate.parse("2020-12-12")) ;
			rslt.add(LocalDate.parse("2018-12-12")) ;
			
		assertEquals(rslt, valeurDate.from(valeur).getValeur());
	}
	
	/*
	@SuppressWarnings("static-access")
	@Test
	void testNotDateFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		ArrayList<String>  date = new ArrayList<String>();
		date.add("1200") ;
		date.add("1200") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(date);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(DateTimeParseException.class,()->valeurDate.from(valeur).getValeur());
	}

	
	@SuppressWarnings("static-access")
	@Test
	void testNullDateFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurDate.from(valeur).getValeur());
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testBadFormat() {
		
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		ArrayList<String> date = new ArrayList<String>();
		date.add("12-12-2020") ;
		date.add("12-12-2020") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(date);
				
		//Exection et Veriication 
		assertThrows(DateTimeParseException.class,()->valeurDate.from(valeur).getValeur());
	}*/
	

}
