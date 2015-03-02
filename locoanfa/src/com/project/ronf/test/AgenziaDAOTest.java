package com.project.ronf.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Agenzia;

public class AgenziaDAOTest {

	@Test
	public void test() {
		Agenzia agenziaTest = new Agenzia("Via Della Prova", "agenziaTest",
				false, "http://localhost:8080/locoprova");

		AgenziaServlet.agenziaDAO.add(agenziaTest);

		// Test findByNome
		Agenzia ag = AgenziaServlet.agenziaDAO.findByNome("agenziaTest");
		assertEquals(agenziaTest, ag);
		AgenziaServlet.agenziaDAO.remove(agenziaTest);

	}

}
