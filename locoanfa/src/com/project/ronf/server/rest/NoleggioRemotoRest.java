package com.project.ronf.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.project.ronf.server.AgenziaServlet;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;

/**
 * Servizi rest per il noleggio con destinazione remota
 */
@Path("/noleggioremoto")
public class NoleggioRemotoRest {

	/**
	 * Ripristina il cliente rimosso dalla agenzia di partenza
	 * 
	 * @param cliente
	 *            il cliente da ripristinare
	 * @return una risposta http con stato 200
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("/restorecliente")
	public Response restoreCliente(Cliente cliente) {
		AgenziaServlet.clienteDAO.add(cliente);
		return Response.status(200).build();
	}

	/**
	 * Registra il noleggio sull'agenzia di arrivo
	 * 
	 * @param noleggio
	 *            il noleggio da registrare
	 * @return una risposta http con stato 200
	 */
	@POST
	@Path("/store")
	@Consumes(MediaType.APPLICATION_XML)
	public Response regNoleggioRemoto(Noleggio noleggio) {

		Cliente cli = AgenziaServlet.clienteDAO.getCliente(noleggio
				.getCliente().getNumeroPatente());
		List<Cliente> guidAgg = new ArrayList<Cliente>();
		List<Optional> listOptional = new ArrayList<Optional>();
		for (Cliente guid : noleggio.getGuidatoriAggiuntivi()) {
			guidAgg.add(AgenziaServlet.clienteDAO.getCliente(guid
					.getNumeroPatente()));
		}
		
		for (Optional optional : noleggio.getOptional())
		{
			System.out.println(optional.getTipo());
			Optional currentOptional = new Optional(optional.getTipo());
			listOptional.add(currentOptional);
			AgenziaServlet.optionalDAO.add(currentOptional);
		}
		
		noleggio.setGuidatoriAggiuntivi(guidAgg);
		noleggio.setOptional(listOptional);
		noleggio.setCliente(cli);

		Agenzia partenza = AgenziaServlet.agenziaDAO.findByNome(noleggio
				.getAgenziaPartenza().getName());
		Agenzia arrivo = AgenziaServlet.agenziaDAO.findByNome(noleggio
				.getAgenziaArrivo().getName());

		noleggio.setAgenziaPartenza(partenza);
		noleggio.setAgenziaArrivo(arrivo);

		AgenziaServlet.autoDAO.add(noleggio.getAuto());
		AgenziaServlet.noleggioDAO.add(noleggio);
		return Response.status(200).build();
	}

}
