package com.project.ronf.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.project.ronf.shared.entities.Auto;

public class AutoTest {

	@Test
	public void test() {
		Auto autoTest = new Auto("modelloTest", "TS1234", 0);

		// Test getModello
		String modTest = autoTest.getModello();
		assertEquals("modelloTest", modTest);

		// Test getTarga
		String targaTest = autoTest.getTarga();
		assertEquals("TS1234", targaTest);

		// Test getTipo
		String tipoTest = Auto.tipi[autoTest.getTipo()];
		assertEquals("mini", tipoTest);

		// Test setTarga
		autoTest.setTarga("PR158ST");
		assertEquals("PR158ST", autoTest.getTarga());

		// Test prezzo
		int prezzoTest = (int) Auto.prezzo[0];
		assertEquals(10, prezzoTest);
	}

}
