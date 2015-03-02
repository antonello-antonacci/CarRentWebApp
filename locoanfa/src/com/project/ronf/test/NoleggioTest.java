package com.project.ronf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;

public class NoleggioTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws ParseException {

		Agenzia agenziaTestPar = new Agenzia("Via Della Prova", "agenziaPar",
				true, "http://localhost:8080/locoprova");
		Agenzia agenziaTestDst = new Agenzia("Via Test", "agenziaDst", false,
				"http://localhost:8082/locoprova2");
		Auto autoTest = new Auto("modelloTest", "TS1234", 0);
		Cliente clienteTest = new Cliente("nomeTest", "cognomeTest",
				"Via Test", "TS1587");
		Optional opt1 = new Optional(1);
		Optional opt2 = new Optional(2);
		ArrayList<Optional> opt = new ArrayList<Optional>();
		Date start = new Date(11, 0, 1);
		Date end = new Date(11, 0, 10);

		Noleggio nolTest = new Noleggio(autoTest, clienteTest, start, end,
				agenziaTestDst, agenziaTestPar);

		// Test setOptional
		opt.add(opt1);
		opt.add(opt2);
		nolTest.setOptional(opt);
		assertNotNull(nolTest.getOptional());

		// Test getOptional
		ArrayList<Optional> tipiOptTest = new ArrayList<Optional>();
		tipiOptTest = (ArrayList<Optional>) nolTest.getOptional();
		assertNotNull(tipiOptTest);

		// Test getAgenziaPartenza
		Agenzia partenza = nolTest.getAgenziaPartenza();
		assertEquals(agenziaTestPar, partenza);

		// Test setAgenziaPartenza
		Agenzia setagPar = new Agenzia("Via partenza", "agPar", true,
				"http://localhost:8080/locopar");
		nolTest.setAgenziaPartenza(setagPar);
		assertEquals(setagPar, nolTest.getAgenziaPartenza());

		// Test getAgenziaArrivo
		Agenzia arrivo = nolTest.getAgenziaArrivo();
		assertEquals(agenziaTestDst, arrivo);

		// Test setAgenziaArrivo
		Agenzia setAgDst = new Agenzia("Via arrivo", "agDst", false,
				"http://localhost:8082/locoDst");
		nolTest.setAgenziaArrivo(setAgDst);
		assertEquals(setAgDst, nolTest.getAgenziaArrivo());

		// Test getCliente
		Cliente cliTest = nolTest.getCliente();
		assertEquals(clienteTest, cliTest);

		// Test setCliente
		Cliente setCli = new Cliente("setCliTest", "cognomeSet", "Via Set",
				"CS0000");
		nolTest.setCliente(setCli);
		assertEquals(setCli, nolTest.getCliente());

		// Test getGiorni
		long giorni = nolTest.getGiorni();
		assertEquals(10, giorni);

		// Test getRifornimento
		assertFalse(nolTest.getRifornimento());

		// Test setRifornimento
		nolTest.setRifornimento(true);
		assertTrue(nolTest.getRifornimento());

		// Test getRiconsegna
		assertFalse(nolTest.getRiconsegna());

		// Test setRiconsegna
		nolTest.setRiconsegna(true);
		assertTrue(nolTest.getRiconsegna());

		// Test getPartenza
		long parTest = nolTest.getPartenza();
		assertEquals(start.getTime(), parTest);

		// Test getArrivo
		long arrTest = nolTest.getArrivo();
		assertEquals(end.getTime(), arrTest);

		// Test getGuidatoriAggiuntivi
		List<Cliente> guiAggTest = nolTest.getGuidatoriAggiuntivi();
		List<Cliente> ListaVuota = new ArrayList<Cliente>();
		assertEquals(ListaVuota, guiAggTest);

		// Test setGuidatoriAggiuntivi
		guiAggTest.add(setCli);
		nolTest.setGuidatoriAggiuntivi(guiAggTest);
		assertNotNull(nolTest.getGuidatoriAggiuntivi());

		// Test getAuto
		Auto carTest = nolTest.getAuto();
		assertEquals(autoTest, carTest);

		// Test getPrice senza optional
		Noleggio nolPriceTest = new Noleggio(autoTest, clienteTest, start, end,
				agenziaTestDst, agenziaTestPar);
		ArrayList<Optional> optPrice = new ArrayList<Optional>();
		int prezzoTest = (int) nolPriceTest.getPrice();
		assertEquals(100, prezzoTest);

		// Test getPrice con guidatori aggiuntivi
		nolPriceTest.setGuidatoriAggiuntivi(guiAggTest);
		prezzoTest = (int) nolPriceTest.getPrice();
		assertEquals(120, prezzoTest);

		// Test getPrice con guidatori aggiuntivi e optional
		optPrice.add(new Optional(0));
		optPrice.add(new Optional(1));
		nolPriceTest.setOptional(optPrice);
		prezzoTest = (int) nolPriceTest.getPrice();
		assertEquals(160, prezzoTest);

		// Test con aggiunta rifornimento
		nolPriceTest.setRifornimento(true);
		prezzoTest = (int) nolPriceTest.getPrice();
		assertEquals(210, prezzoTest);

	}

}
