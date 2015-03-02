package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Agenzia;

@RemoteServiceRelativePath("gestagenzia")
public interface GestioneAgenzia extends RemoteService {
	public List<Agenzia> listaAgenzia();
}
