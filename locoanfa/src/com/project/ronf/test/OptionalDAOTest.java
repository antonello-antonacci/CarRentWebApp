package com.project.ronf.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Optional;

public class OptionalDAOTest {

	@Test
	public void test() {

		Optional opt0 = new Optional(0);
		Optional opt1 = new Optional(1);

		AgenziaServlet.optionalDAO.add(opt0);
		AgenziaServlet.optionalDAO.add(opt1);

		// Test getAllByType
		List<Optional> listOpt0 = AgenziaServlet.optionalDAO.getAllByType(0);
		assertNotNull(listOpt0);

		List<Optional> listOpt1 = AgenziaServlet.optionalDAO.getAllByType(1);
		assertNotNull(listOpt1);

		AgenziaServlet.optionalDAO.remove(opt0);
		AgenziaServlet.optionalDAO.remove(opt1);
	}

}
