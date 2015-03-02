package com.project.ronf.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.entities.Cliente;

public class ClienteDAO extends GenericDAO<Cliente> {

	/**
	 * @param noPatente
	 *            il numero di patente del cliente cercato
	 * @return il cliente con il numero di patente specificato
	 */
	public Cliente getCliente(String noPatente) {
		TypedQuery<Cliente> patenteQuery = em.createQuery(
				"SELECT c FROM Cliente c WHERE c.numeroPatente = :noPatente",
				Cliente.class);
		patenteQuery.setParameter("noPatente", noPatente);
		try {
			return patenteQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/*
	 * Ritorna tutti i clienti che non hanno noleggi pendenti
	 */
	public List<Cliente> getAll() {
		TypedQuery<Cliente> guidAggiuntiviQuery = em.createQuery(
				"SELECT n.guidatoriAggiuntivi.id FROM Noleggio n",
				Cliente.class);

		TypedQuery<Cliente> clienteNoleggioQuery = em.createQuery(
				"SELECT n.cliente.id FROM Noleggio n", Cliente.class);
		TypedQuery<Cliente> clienteQuery = em
				.createQuery(
						"SELECT c FROM Cliente c WHERE (c.id NOT in :clientioccupati) AND (c.id NOT in :guidAggiuntivi) ",
						Cliente.class);
		try {
			clienteQuery.setParameter("clientioccupati",
					clienteNoleggioQuery.getResultList());
			clienteQuery.setParameter("guidAggiuntivi",
					guidAggiuntiviQuery.getResultList());
			return clienteQuery.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Cliente>();
		}
	}

	/**
	 * @param noPatente
	 *            il numero di patente del cliente cercato
	 * @return se il cliente con numero di patente specificato è presente nel
	 *         database
	 */
	public boolean containsCliente(String noPatente) {
		return getCliente(noPatente) != null;
	}
}
