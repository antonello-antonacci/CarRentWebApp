package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;

public interface GestioneAutoAsync {
	public void listaAuto(AsyncCallback<List<Auto>> callback);

	public void registraAuto(String modello, String targa, int tipo,
			AsyncCallback<Auto> callback); // prova

	void elencoAuto(Agenzia target, AsyncCallback<List<Auto>> callback);

	void richiestaAuto(Agenzia target, Auto richiesta,
			AsyncCallback<Boolean> callback);

}
