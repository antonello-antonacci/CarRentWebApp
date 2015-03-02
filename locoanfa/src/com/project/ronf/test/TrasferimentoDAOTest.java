package com.project.ronf.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Trasferimento;

public class TrasferimentoDAOTest {

	@Test
	public void test() {

		Auto autoTesttrasf = new Auto("ModProva", "TargaProva", 0);
		Agenzia ag = AgenziaServlet.agenziaDAO.getLocale();
		Agenzia agenziaTest = new Agenzia("Via Della Prova", "agenziaTest",
				false, "http://localhost:8080/locoprova");

		AgenziaServlet.autoDAO.add(autoTesttrasf);
		AgenziaServlet.agenziaDAO.add(agenziaTest);

		Trasferimento trasfTest = new Trasferimento(ag, agenziaTest,
				autoTesttrasf.getTarga(), autoTesttrasf.getModello(),
				autoTesttrasf.getTipo());

		AgenziaServlet.trasferimentoDAO.add(trasfTest);

		List<Trasferimento> trasf = AgenziaServlet.trasferimentoDAO
				.getTrasferimenti("partenza");

		assertNotNull(trasf);

		AgenziaServlet.trasferimentoDAO.remove(trasfTest);
		AgenziaServlet.agenziaDAO.remove(agenziaTest);
		AgenziaServlet.autoDAO.remove(autoTesttrasf);

	}

}
