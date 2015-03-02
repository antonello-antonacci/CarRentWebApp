package com.project.ronf.server.dao;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.DipFrontOffice;
import com.project.ronf.shared.entities.DipTrasferimento;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;

public class NoleggioDAO extends GenericDAO<Noleggio> {

	/**
	 * @param dip
	 *            Il dipendente che richiede i noleggi
	 * @return I noleggi riconsegnati per il dipendente del front office, non
	 *         riconsegnati per l'addetto al rifornimento
	 */
	public List<Noleggio> getAllNoleggi(Dipendente dip) {

		TypedQuery<Noleggio> query;
		if (dip instanceof DipFrontOffice) {
			query = em.createQuery(
					"SELECT n FROM Noleggio n WHERE n.agenziaArrivo.id = "
							+ Long.toString(dip.agenzia.getId()) + " "
							+ "AND n.riconsegna", Noleggio.class);
		} else if (dip instanceof DipTrasferimento) {
			query = em.createQuery(
					"SELECT n FROM Noleggio n WHERE " + "n.agenziaArrivo.id = "
							+ Long.toString(dip.agenzia.getId()) + " "
							+ "AND NOT (n.riconsegna)", Noleggio.class);
		} else {
			return getAll();
		}
		List<Noleggio> resultList = query.getResultList();
		for (Noleggio result : resultList) {
			em.detach(result);
		}
		return resultList;
	}

	/**
	 * @param cl
	 *            Il cliente da controllare
	 * @return Se il cliente ha qualche noleggio pendente
	 */
	public boolean checkPendingNoleggi(Cliente cl) {
		TypedQuery<Noleggio> noleggioCliQuery = em
				.createQuery(
						"SELECT n FROM Noleggio WHERE n.cliente.numeroPatente = :noPatenteCliente",
						Noleggio.class);
		noleggioCliQuery
				.setParameter("noPatenteCliente", cl.getNumeroPatente());
		try {
			return !noleggioCliQuery.getResultList().isEmpty();
		} catch (PersistenceException exc) {
			return false;
		}
	}

	/**
	 * Registra l'avvenuta riconsegna relativa ad un noleggio
	 * 
	 * @param noleggioId
	 *            L'id del noleggio per cui si vuole registrare la riconsegna
	 * @param rifornimento
	 *            se è stato necessario un rifornimento
	 */
	public void registerRiconsegna(long noleggioId, boolean rifornimento) {
		Noleggio toModify = em.find(Noleggio.class, noleggioId);
		em.getTransaction().begin();
		toModify.setRiconsegna(true);
		toModify.setRifornimento(rifornimento);
		em.getTransaction().commit();
	}
}
