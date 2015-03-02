package com.project.ronf.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.project.ronf.shared.entities.Cliente;

public class ClienteTest {

	@Test
	public void test() {

		Cliente clienteTest = new Cliente("nomeTest", "cognomeTest",
				"Via Test", "TS1587");

		// Test getNome
		String nomeTest = clienteTest.getNome();
		assertEquals("nomeTest", nomeTest);

		// Test getCognome
		String cognTest = clienteTest.getCognome();
		assertEquals("cognomeTest", cognTest);

		// Test getIndirizzo
		String indTest = clienteTest.getIndirizzo();
		assertEquals("Via Test", indTest);

		// Test getNopatente
		String nopatenteTest = clienteTest.getNumeroPatente();
		assertEquals("TS1587", nopatenteTest);

		// Test setIndirizzo
		clienteTest.setIndirizzo("Via set");
		assertEquals("Via set", clienteTest.getIndirizzo());

		// Test setNome
		clienteTest.setNome("Nome set");
		assertEquals("Nome set", clienteTest.getNome());

		// Test setCognome
		clienteTest.setCognome("Cognome set");
		assertEquals("Cognome set", clienteTest.getCognome());

		// Test setNumeroPatente
		clienteTest.setNumeroPatente("ST1234");
		assertEquals("ST1234", clienteTest.getNumeroPatente());

	}

}
