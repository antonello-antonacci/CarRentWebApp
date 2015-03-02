package com.project.ronf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Dipendente;

public class DipendenteDAOTest {

	@Test
	public void test() {

		Dipendente testDip = new Dipendente("NomeProva", "PassProva", null);
		AgenziaServlet.dipendenteDAO.add(testDip);

		// Test getDipendente
		Dipendente dip = AgenziaServlet.dipendenteDAO
				.getDipendente("NomeProva");
		assertEquals(testDip, dip);

		// Test checkUserPass
		boolean check = AgenziaServlet.dipendenteDAO.checkUserPassDipendente(
				"NomeProva", "PassProva");
		assertTrue(check);

		AgenziaServlet.dipendenteDAO.remove(testDip);

	}

}
