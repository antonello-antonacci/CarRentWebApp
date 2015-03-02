package com.project.ronf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.DipFrontOffice;
import com.project.ronf.shared.entities.DipTrasferimento;
import com.project.ronf.shared.entities.Noleggio;

public class NoleggioDAOTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws ParseException {

		Agenzia agenziaPar = new Agenzia("Via Partenza", "agPar", true,
				"http://localhost:8080/locopar");
		Agenzia agenziaDst = new Agenzia("Via Arrivo", "agDst", false,
				"http://localhost:8082/locopdst");
		Auto autoTest = new Auto("modelloTest", "TS1234", 0);
		Auto autoTest2 = new Auto("mod", "TR0000", 1);
		Cliente clienteTest = new Cliente("nomeTest", "cognomeTest",
				"Via Test", "TS1587");
		Cliente clienteTest2 = new Cliente("nome", "cognome", "Via Prova",
				"PR0000");
		DipFrontOffice testDip = new DipFrontOffice("NomeProva", "PassProva",
				agenziaDst);
		DipTrasferimento testDip2 = new DipTrasferimento("NomeProva",
				"PassProva", agenziaDst);
		Date start = new Date(11, 0, 1);
		Date end = new Date(11, 0, 10);

		AgenziaServlet.agenziaDAO.add(agenziaDst);
		AgenziaServlet.agenziaDAO.add(agenziaPar);
		AgenziaServlet.autoDAO.add(autoTest);
		AgenziaServlet.autoDAO.add(autoTest2);
		AgenziaServlet.clienteDAO.add(clienteTest);
		AgenziaServlet.clienteDAO.add(clienteTest2);
		AgenziaServlet.dipendenteDAO.add(testDip);
		AgenziaServlet.dipendenteDAO.add(testDip2);

		Noleggio nolTest = new Noleggio(autoTest, clienteTest, start, end,
				agenziaDst, agenziaPar);
		Noleggio nolTest2 = new Noleggio(autoTest2, clienteTest2, start, end,
				agenziaDst, agenziaPar);
		nolTest.setRiconsegna(false);
		nolTest2.setRiconsegna(true);
		AgenziaServlet.noleggioDAO.add(nolTest);
		AgenziaServlet.noleggioDAO.add(nolTest2);

		// Test getAllNoleggi
		List<Noleggio> nol = AgenziaServlet.noleggioDAO.getAllNoleggi(testDip);
		assertEquals(1, nol.size());

		nol = AgenziaServlet.noleggioDAO.getAllNoleggi(testDip2);
		assertEquals(1, nol.size());

		// Test chekPendingNoleggi
		AgenziaServlet.noleggioDAO.remove(nolTest);
		boolean check = AgenziaServlet.noleggioDAO
				.checkPendingNoleggi(clienteTest);
		assertFalse(check);

		// Test registerRiconsegna
		AgenziaServlet.noleggioDAO.registerRiconsegna(nolTest2.getId(), true);
		nolTest2.setRifornimento(true);
		boolean rifornimento = nolTest2.getRifornimento();
		assertTrue(rifornimento);
		assertTrue(nolTest2.getRiconsegna());

		AgenziaServlet.noleggioDAO.remove(nolTest2);
		AgenziaServlet.dipendenteDAO.remove(testDip);
		AgenziaServlet.dipendenteDAO.remove(testDip2);
		AgenziaServlet.clienteDAO.remove(clienteTest);
		AgenziaServlet.clienteDAO.remove(clienteTest2);
		AgenziaServlet.autoDAO.remove(autoTest);
		AgenziaServlet.autoDAO.remove(autoTest2);
		AgenziaServlet.agenziaDAO.remove(agenziaPar);
		AgenziaServlet.agenziaDAO.remove(agenziaDst);
	}

}
