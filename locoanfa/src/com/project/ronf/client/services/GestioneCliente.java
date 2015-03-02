package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Cliente;

@RemoteServiceRelativePath("gestcliente")
public interface GestioneCliente extends RemoteService {
	public List<Cliente> listaClienti();

	public Cliente registraCliente(String nome, String cognome,
			String indirizzo, String patente);

	public boolean rimuoviCliente(Cliente cl);
}
