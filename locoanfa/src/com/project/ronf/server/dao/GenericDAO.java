package com.project.ronf.server.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public abstract class GenericDAO<T> {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("global");
	EntityManager em = emf.createEntityManager();

	Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		ParameterizedType superclass = (ParameterizedType) getClass()
				.getGenericSuperclass();

		entityClass = (Class<T>) superclass.getActualTypeArguments()[0];
	}

	/**
	 * @param id
	 *            l'id dell'oggetto cercato
	 * @return l'oggetto con id specificato
	 */
	public T get(long id) {
		TypedQuery<T> idQuery = em.createQuery(
				"SELECT o FROM" + entityClass.getCanonicalName()
						+ "o WHERE o.id = :id", entityClass);
		idQuery.setParameter("id", id);
		try {
			return idQuery.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}

	/**
	 * @return tutti gli oggetti di tipo T
	 */
	public List<T> getAll() {
		TypedQuery<T> allQuery = em.createQuery(
				"SELECT o FROM " + entityClass.getCanonicalName() + " o",
				entityClass);
		try {
			return allQuery.getResultList();
		} catch (NoResultException e) {
			return (new ArrayList<T>());
		}
	}

	/**
	 * Memorizza un oggetto nel database
	 * 
	 * @param obj
	 *            l'oggetto da memorizzare nel database
	 */
	public void add(T obj) {
		em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
	}

	/**
	 * Rimuove un oggetto dal database
	 * 
	 * @param obj
	 *            L'oggetto da rimuovere
	 */
	public void remove(T obj) {
		em.getTransaction().begin();
		em.remove(em.merge(obj));
		em.getTransaction().commit();
	}

	/**
	 * Rimuove tutti gli oggetti di un certo tipo dal database
	 */
	public void removeAll() {
		List<T> toRemove = getAll();
		removeAllList(toRemove);
	}

	/**
	 * Rimuove tutti gli oggetti di una lista dal database
	 * 
	 * @param toRemove
	 *            una lista di oggetti da rimuovere
	 */
	public void removeAllList(List<T> toRemove) {
		for (T obj : toRemove) {
			remove(obj);
		}
	}

	/**
	 * Aggiorna i campi di un oggetto nel database
	 * 
	 * @param obj
	 *            l'oggetto da aggiornare
	 */
	public void update(T obj) {
		em.getTransaction().begin();
		em.merge(obj);
		em.getTransaction().commit();
	}

}
