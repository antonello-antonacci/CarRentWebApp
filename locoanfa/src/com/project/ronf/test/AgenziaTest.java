package com.project.ronf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.project.ronf.shared.entities.Agenzia;

public class AgenziaTest {

	@Test
	public void test() {
		Agenzia agenziaTest = new Agenzia("Via Della Prova", "agenziaTest",
				false, "http://localhost:8080/locoprova");

		// Test getName
		String nome = agenziaTest.getName();
		assertEquals("agenziaTest", nome);

		// Test getAdress
		String indirizzo = agenziaTest.getAddress();
		assertEquals("Via Della Prova", indirizzo);

		// Test isLocale
		boolean localeTest = agenziaTest.isLocale();
		assertFalse(localeTest);

		// Test getRestUri
		String uri = agenziaTest.getRestUri();
		assertEquals("http://localhost:8080/locoprova", uri);

		// Test setRestUri
		agenziaTest.setRestUri("Prova Uri");
		assertEquals("Prova Uri", agenziaTest.getRestUri());

		// Test setLocale
		agenziaTest.setLocale(true);
		assertTrue(agenziaTest.isLocale());

		// Test setAddress
		agenziaTest.setAddress("Indirizzo prova");
		assertEquals("Indirizzo prova", agenziaTest.getAddress());

		// Test setName
		agenziaTest.setName("Prova");
		assertEquals("Prova", agenziaTest.getName());

	}

}
