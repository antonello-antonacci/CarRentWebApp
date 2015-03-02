package com.project.ronf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Auto;

public class AutoDAOTest {

	@Test
	public void test() {

		Auto autoTest = new Auto("ModTest", "TargaTest", 0);
		AgenziaServlet.autoDAO.add(autoTest);

		// Test getAuto
		Auto auto = AgenziaServlet.autoDAO.getAuto("TargaTest");
		assertEquals(autoTest, auto);

		// Test getAll
		Auto autoTest2 = new Auto("ModT", "TargaT", 1);
		AgenziaServlet.autoDAO.add(autoTest2);
		List<Auto> listaAuto = AgenziaServlet.autoDAO.getAll();
		assertNotNull(listaAuto);

		// Test containsAuto
		boolean contains = AgenziaServlet.autoDAO.containsAuto("TargaTest");
		assertTrue(contains);

		AgenziaServlet.autoDAO.remove(autoTest);
		AgenziaServlet.autoDAO.remove(autoTest2);

		contains = AgenziaServlet.autoDAO.containsAuto("TargaTest");
		assertFalse(contains);
	}

}
