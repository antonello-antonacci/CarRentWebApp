package com.project.ronf.server.dao;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.ronf.shared.BCrypt;
import com.project.ronf.shared.entities.Dipendente;

public class DipendenteDAO extends GenericDAO<Dipendente> {

	/**
	 * @param userDip
	 *            l'username del dipendente cercato
	 * @return il dipendente con username specificato
	 */
	public Dipendente getDipendente(String userDip) {
		TypedQuery<Dipendente> userQuery = em.createQuery(
				"SELECT d FROM Dipendente d WHERE d.user =:user",
				Dipendente.class);
		userQuery.setParameter("user", userDip);
		try {
			return userQuery.getSingleResult();
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param user
	 *            l'username da controllare
	 * @param pass
	 *            la password da controllare
	 * @return se le credenziali sono valide
	 */
	public boolean checkUserPassDipendente(String user, String pass) {
		Dipendente myDip = getDipendente(user);
		if (myDip != null)
			return BCrypt.checkpw(pass, myDip.getPass());
		else
			return false;
	}
}
