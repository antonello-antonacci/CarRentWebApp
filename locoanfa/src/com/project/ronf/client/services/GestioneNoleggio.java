package com.project.ronf.client.services;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;

@RemoteServiceRelativePath("gestnoleggio")
public interface GestioneNoleggio extends RemoteService {
	public Noleggio nuovoNoleggio(Cliente cli, Auto car, Date start, Date end,
			Agenzia dest, List<Optional> lopt, List<Cliente> lguid);

	public List<Noleggio> listaNoleggi(Dipendente dip);

	public void fineNoleggio(Noleggio n);

	void registraRiconsegna(long id, boolean riconsegna);

	List<Optional> getAllOptional();

}
