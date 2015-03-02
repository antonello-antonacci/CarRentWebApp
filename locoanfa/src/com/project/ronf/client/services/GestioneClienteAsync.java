package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Cliente;

public interface GestioneClienteAsync {
	public void registraCliente(String nome, String cognome, String indirizzo,
			String patente, AsyncCallback<Cliente> callback);

	void listaClienti(AsyncCallback<List<Cliente>> callback);

	public void rimuoviCliente(Cliente cl, AsyncCallback<Boolean> callback);
}
