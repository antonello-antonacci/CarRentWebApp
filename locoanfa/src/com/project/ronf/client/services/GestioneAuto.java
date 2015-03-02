package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;

@RemoteServiceRelativePath("gestauto")
public interface GestioneAuto extends RemoteService {
	public List<Auto> listaAuto();

	public Auto registraAuto(String modello, String targa, int tipo);

	public List<Auto> elencoAuto(Agenzia target);

	public Boolean richiestaAuto(Agenzia target, Auto richiesta);
}
