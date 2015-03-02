package com.project.ronf.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Cliente;

public class ClienteDAOTest {

	@Test
	public void test() {

		Cliente clienteTest = new Cliente("nomeTest", "cognomeTest",
				"Via Test", "TS1587");
		Cliente clienteTest2 = new Cliente("nome", "cognome", "Via Prova",
				"PR0000");
		AgenziaServlet.clienteDAO.add(clienteTest);

		// Test getCliente
		Cliente cli = AgenziaServlet.clienteDAO.getCliente("TS1587");
		assertEquals(clienteTest, cli);

		// Test getAll
		AgenziaServlet.clienteDAO.add(clienteTest2);
		List<Cliente> listCli = AgenziaServlet.clienteDAO.getAll();
		assertNotNull(listCli);

		// Test containsCliente
		boolean contains = AgenziaServlet.clienteDAO.containsCliente("TS1587");
		assertTrue(contains);

		AgenziaServlet.clienteDAO.remove(clienteTest);
		AgenziaServlet.clienteDAO.remove(clienteTest2);

		contains = AgenziaServlet.clienteDAO.containsCliente("TS1587");
		assertFalse(contains);
	}

}
