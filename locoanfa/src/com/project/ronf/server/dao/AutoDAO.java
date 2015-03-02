package com.project.ronf.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.entities.Auto;

public class AutoDAO extends GenericDAO<Auto> {

	/**
	 * @param targa
	 *            la targa dell'auto cercata
	 * @return l'auto con la targa specificata
	 */
	public Auto getAuto(String targa) {
		TypedQuery<Auto> targaQuery = em.createQuery(
				"SELECT a FROM Auto a WHERE a.targa=:targa", Auto.class);
		targaQuery.setParameter("targa", targa);
		try {
			return targaQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/*
	 * Tutte le auto non occupate (quelle che non sono in nessun oggetto
	 * noleggio)
	 */
	public List<Auto> getAll() {
		TypedQuery<Auto> autoOccupate = em.createQuery(
				"SELECT n.auto.targa FROM Noleggio n", Auto.class);
		TypedQuery<Auto> autoLibere = em.createQuery(
				"SELECT a FROM Auto a WHERE a.targa NOT IN :autoOccupate",
				Auto.class);
		try {
			autoLibere.setParameter("autoOccupate",
					autoOccupate.getResultList());
			return autoLibere.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Auto>();
		}
	}

	/**
	 * @param targa
	 *            la targa dell'auto cercata
	 * @return se il database contiene l'auto con la targa specificata
	 */
	public boolean containsAuto(String targa) {
		return getAuto(targa) != null;
	}
}
