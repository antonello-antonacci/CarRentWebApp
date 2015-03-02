package com.project.ronf.client.services;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;

public interface GestioneNoleggioAsync {
	void nuovoNoleggio(Cliente cli, Auto car, Date start, Date end,
			Agenzia dest, List<Optional> lopt, List<Cliente> lguid,
			AsyncCallback<Noleggio> callback);

	public void listaNoleggi(Dipendente dip,
			AsyncCallback<List<Noleggio>> callback);

	public void fineNoleggio(Noleggio n, AsyncCallback<Void> callback);

	public void registraRiconsegna(long id, boolean riconsegna,
			AsyncCallback<Void> asyncCallback);

	void getAllOptional(AsyncCallback<List<Optional>> callback);

}
