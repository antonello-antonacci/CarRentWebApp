package com.project.ronf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project.ronf.shared.entities.Optional;

public class OptionalTest {

	@Test
	public void test() {

		Optional optTest = new Optional(0);
		Optional opt2Test = new Optional(1);

		String tipoTest = Optional.tipi[optTest.getTipo()];

		String tipo2Test = Optional.tipi[opt2Test.getTipo()];

		// Test getTipo
		assertEquals("seggiolino auto", tipoTest);

		assertEquals("navigatore", tipo2Test);

	}

}
