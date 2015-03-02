package com.project.ronf.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.entities.Optional;

public class OptionalDAO extends GenericDAO<Optional> {

	/**
	 * @param type
	 *            Il tipo di optional cercato
	 * @return tutti gli optional del tipo cercato
	 */
	public List<Optional> getAllByType(int type) {
		TypedQuery<Optional> optionalUsati = em.createQuery(
				"SELECT n.optional.id FROM Noleggio n", Optional.class);
		TypedQuery<Optional> optTypeQuery = em
				.createQuery(
						"SELECT o FROM Optional o WHERE o.tipo=:tipo AND o.id NOT IN :optusati",
						Optional.class);
		optTypeQuery.setParameter("tipo", type);
		optTypeQuery.setParameter("optusati", optionalUsati.getResultList());
		try {
			List<Optional> optList = optTypeQuery.getResultList();
			return optList;
		} catch (PersistenceException exc) {
			return new ArrayList<Optional>();
		}
	}
}
