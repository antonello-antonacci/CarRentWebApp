package com.project.ronf.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Trasferimento;

@RemoteServiceRelativePath("gesttrasf")
public interface GestioneTrasferimento extends RemoteService {
	public List<Trasferimento> getTrasferimentiPartenza();

	public List<Trasferimento> getTrasferimentiArrivo();

	public Boolean avvioTrasferimento(Agenzia target, Trasferimento trasf);

	public void rimuoviTrasferimento(Trasferimento trasf);

	public void terminaTrasferimento(Trasferimento trasf);

}
