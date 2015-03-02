package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Trasferimento;

public interface GestioneTrasferimentoAsync {

	void getTrasferimentiPartenza(AsyncCallback<List<Trasferimento>> callback);

	void getTrasferimentiArrivo(AsyncCallback<List<Trasferimento>> callback);

	void avvioTrasferimento(Agenzia target, Trasferimento trasf,
			AsyncCallback<Boolean> callback);

	void rimuoviTrasferimento(Trasferimento trasf, AsyncCallback<Void> callback);

	void terminaTrasferimento(Trasferimento trasf, AsyncCallback<Void> callback);

}
