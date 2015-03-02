package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Agenzia;

public interface GestioneAgenziaAsync {
	public void listaAgenzia(AsyncCallback<List<Agenzia>> callback);
}
