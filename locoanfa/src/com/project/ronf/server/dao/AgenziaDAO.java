package com.project.ronf.server.dao;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.entities.Agenzia;

public class AgenziaDAO extends GenericDAO<Agenzia> {

	/**
	 * @return L'agenzia locale (quella che effettua la chiamata)
	 */
	public Agenzia getLocale() {
		TypedQuery<Agenzia> localeQuery = em.createQuery(
				"SELECT a FROM Agenzia A WHERE a.locale", Agenzia.class);
		try {
			return localeQuery.getSingleResult();
		} catch (PersistenceException E) {
			return null;
		}
	}

	/**
	 * @param nome
	 *            Il nome dell'agenzia da cercare
	 * @return l'agenzia con il nome specificato
	 */
	public Agenzia findByNome(String nome) {
		TypedQuery<Agenzia> nomeQuery = em.createQuery(
				"SELECT a FROM Agenzia a WHERE a.name=:nome", Agenzia.class);
		nomeQuery.setParameter("nome", nome);
		try {
			return nomeQuery.getSingleResult();
		} catch (NoResultException E) {
			return null;
		}
	}
}
