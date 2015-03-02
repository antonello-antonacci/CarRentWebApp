package com.project.ronf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Dipendente;

public class DipendenteTest {

	@Test
	public void test() {

		Agenzia agenziaTest = new Agenzia("Via Della Prova", "agenziaTest",
				false, "http://localhost:8080/locoprova");
		Dipendente testDip = new Dipendente("DipTest", "PassTest", agenziaTest);

		// Test checkPassword
		boolean chkPass = testDip.checkPassword("PassTest");
		assertTrue(chkPass);

		// Test getAgenzia
		Agenzia agTest = testDip.getAgenzia();
		assertEquals(agenziaTest, agTest);

		// Test getUser
		String userTest = testDip.getUser();
		assertEquals("DipTest", userTest);

		// Test getPassword
		String passTest = testDip.getPass();
		assertNotNull(passTest);

		// Test setPassword
		testDip.setPass("password");
		assertNotNull(testDip.getPass());
	}

}
