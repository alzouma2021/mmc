package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurDateTimeTest {
	
	
	ValeurDateTime valeurDateTime ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurDate
		valeurDateTime = new ValeurDateTime();
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void test() {
		
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList dateTime = new ArrayList();
		dateTime.add("2021-12-12T10:10:10") ;
		dateTime.add("2020-12-12T10:11:10") ;
		dateTime.add("2018-12-12T10:12:10") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(dateTime);
		
		//Exection du Test et Verification des sortants
			ArrayList<LocalDateTime> rslt = new ArrayList<LocalDateTime>();
			
				rslt.add(LocalDateTime.of(2021,12,12,10,10,10 )) ;
				rslt.add(LocalDateTime.of(2020,12,12,10,11,10)) ;
				rslt.add(LocalDateTime.of(2018,12,12,10,12,10)) ;
			
		assertEquals(rslt , valeurDateTime.from(valeur).getValeur());
		
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testNotDateTimeFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList dateTime = new ArrayList();
		dateTime.add("nonLocalDateTime") ;
		dateTime.add("nonLocalDateTime") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(dateTime);
		
		//Execution du test et Verification de l'envoi d'une exception
		assertThrows(DateTimeParseException.class,()->valeurDateTime.from(valeur).getValeur());
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNullDateFrom() {
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurDateTime.from(valeur).getValeur());
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testBadFormat() {
		
		//fail("Not yet implemented");
		
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList dateTime = new ArrayList();
		dateTime.add("12-12-20201T10:10:10") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(dateTime);
				
		//Exection et Veriication 
		assertThrows(DateTimeParseException.class,()->valeurDateTime.from(valeur).getValeur());
	}
	
}
