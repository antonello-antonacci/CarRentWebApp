package com.project.ronf.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Dipendente;

public interface AutenticazioneAsync {

	void login(String user, String pass, AsyncCallback<Dipendente> callback);

	void loginSessione(AsyncCallback<Dipendente> callback);

	void logOut(AsyncCallback<Void> callback);

}
