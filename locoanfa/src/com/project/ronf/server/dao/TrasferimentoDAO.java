package com.project.ronf.server.dao;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import java.util.ArrayList;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Trasferimento;

public class TrasferimentoDAO extends GenericDAO<Trasferimento> {

	/**
	 * @param tipo
	 *            "arrivo" o "partenza"
	 * @return i trasferimenti in arrivo o in partenza
	 */
	public List<Trasferimento> getTrasferimenti(String tipo) {
		String nomeAgenzia = AgenziaServlet.agenziaDAO.getLocale().getName();
		TypedQuery<Trasferimento> trasfQuery = em.createQuery(
				"SELECT t FROM Trasferimento t WHERE t." + tipo + ".name = \'"
						+ nomeAgenzia + "\'", Trasferimento.class);
		try {
			return (ArrayList<Trasferimento>) trasfQuery.getResultList();
		} catch (PersistenceException e) {
			return new ArrayList<Trasferimento>();
		}
	}
}
