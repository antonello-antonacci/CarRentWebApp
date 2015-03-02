package com.project.ronf.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Dipendente;

@RemoteServiceRelativePath("login")
public interface Autenticazione extends RemoteService {
	Dipendente login(String user, String pass) throws IllegalArgumentException;

	Dipendente loginSessione() throws IllegalArgumentException;

	void logOut();
}