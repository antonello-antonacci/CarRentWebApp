package com.project.ronf.server.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Trasferimento;

/**
 * Servizi rest per i trasferimenti
 */
@Path("/trasf")
public class TrasferimentiRest {

	/**
	 * Il trasferimento è avviato, lo trasmette all'agenzia di partenza
	 * 
	 * @param trasf
	 *            il trasferimento avviato
	 * @return risposta http con stato 200
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("/store")
	public Response storeTrasferimento(Trasferimento trasf) {
		Agenzia part = AgenziaServlet.agenziaDAO.findByNome(trasf.getPartenza()
				.getName());
		Agenzia dest = AgenziaServlet.agenziaDAO.findByNome(trasf
				.getDestinazione().getName());
		trasf.setDestinazione(dest);
		trasf.setPartenza(part);
		AgenziaServlet.trasferimentoDAO.add(trasf);
		return Response.status(200).build();
	}

	/**
	 * Richiede il trasferimento selezionato all'agenzia di origine
	 * 
	 * @param trasf
	 *            il trasferimento desiderato
	 * @return risposta http con stato 200
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("/request")
	public Response requestTrasferimento(Trasferimento trasf) {
		Auto richiesta = AgenziaServlet.autoDAO.getAuto(trasf.getTargaAuto());
		Agenzia part = AgenziaServlet.agenziaDAO.findByNome(trasf.getPartenza()
				.getName());
		Agenzia dest = AgenziaServlet.agenziaDAO.findByNome(trasf
				.getDestinazione().getName());
		trasf.setDestinazione(dest);
		trasf.setPartenza(part);
		AgenziaServlet.autoDAO.remove(richiesta);
		AgenziaServlet.trasferimentoDAO.add(trasf);
		return Response.status(200).build();
	}

	/**
	 * @return la lista di auto disponibili dell'agenzia
	 */
	@GET
	@Path("/getallauto")
	@Produces(MediaType.APPLICATION_XML)
	public List<Auto> getAllAuto() {
		List<Auto> retval = AgenziaServlet.autoDAO.getAll();
		return retval;
	}
}
