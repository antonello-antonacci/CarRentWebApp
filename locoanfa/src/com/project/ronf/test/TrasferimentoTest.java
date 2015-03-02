package com.project.ronf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Trasferimento;

public class TrasferimentoTest {

	@Test
	public void test() {

		Agenzia agenziaPartenzaTest = new Agenzia("Via Della Prova",
				"agenziaTest", true, "http://localhost:8080/locoprova");
		Agenzia agenziaArrivoTest = new Agenzia("Via Del Test", "agenziaProva",
				false, "http://localhost:8080/locoprova2");
		Trasferimento trasfTest = new Trasferimento(agenziaPartenzaTest,
				agenziaArrivoTest, "TR123", "ModelloTest", 0);

		// Test getDestinazione
		Agenzia agArrTest = new Agenzia();
		agArrTest = trasfTest.getDestinazione();
		assertEquals(agenziaArrivoTest, agArrTest);

		// Test setDestinazione
		Agenzia setArrTest = new Agenzia("Arrivo", "setArrivo", false,
				"http://localhost:8082/locoarr");
		trasfTest.setDestinazione(setArrTest);
		assertEquals(setArrTest, trasfTest.getDestinazione());

		// Test getPartenza
		Agenzia agParTest = new Agenzia();
		agParTest = trasfTest.getPartenza();
		assertEquals(agenziaPartenzaTest, agParTest);

		// Test getTargaAuto
		String targaTest = trasfTest.getTargaAuto();
		assertEquals("TR123", targaTest);

		// Test getModelloAuto
		String modTest = trasfTest.getModelloAuto();
		assertEquals("ModelloTest", modTest);

		// Test getTipoAuto
		int tipoTest = trasfTest.getTipoAuto();
		assertEquals(0, tipoTest);

		// Test setPartenza
		Agenzia setParTest = new Agenzia("Partenza", "setPartenza", true,
				"http://localhost:8080/locopar");
		trasfTest.setPartenza(setParTest);
		assertEquals(setParTest, trasfTest.getPartenza());

		/*
		 * Agenzia agParTest = new Agenzia();
		 * 
		 * 
		 * 
		 * agParTest = trasfTest.getAgenziaPartenza(); agArrTest =
		 * trasfTest.getAgenziaArrivo();
		 * 
		 * //Test getAgenziaPartenza
		 * assertEquals(agenziaPartenzaTest,agParTest);
		 * 
		 * //Test getAgenziaArrivo assertEquals(agenziaArrivoTest, agArrTest);
		 * 
		 * //Test getTarga
		 * 
		 * //Test getModello
		 * 
		 * 
		 * //Test getTipo
		 */

	}

}
