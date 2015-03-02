package com.project.ronf.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.project.ronf.client.services.Autenticazione;
import com.project.ronf.client.services.GestioneAgenzia;
import com.project.ronf.client.services.GestioneAuto;
import com.project.ronf.client.services.GestioneCliente;
import com.project.ronf.client.services.GestioneNoleggio;
import com.project.ronf.client.services.GestioneTrasferimento;
import com.project.ronf.server.dao.AgenziaDAO;
import com.project.ronf.server.dao.AutoDAO;
import com.project.ronf.server.dao.ClienteDAO;
import com.project.ronf.server.dao.DipendenteDAO;
import com.project.ronf.server.dao.NoleggioDAO;
import com.project.ronf.server.dao.OptionalDAO;
import com.project.ronf.server.dao.TrasferimentoDAO;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;
import com.project.ronf.shared.entities.Trasferimento;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AgenziaServlet extends RemoteServiceServlet implements
		Autenticazione, GestioneAuto, GestioneNoleggio, GestioneAgenzia,
		GestioneCliente, GestioneTrasferimento {

	boolean login;

	// l'oggetto agenzia legato a questo server
	public static Agenzia thisAgenzia;

	// i dao per le entity
	public static AgenziaDAO agenziaDAO = new AgenziaDAO();
	public static AutoDAO autoDAO = new AutoDAO();
	public static ClienteDAO clienteDAO = new ClienteDAO();
	public static DipendenteDAO dipendenteDAO = new DipendenteDAO();
	public static NoleggioDAO noleggioDAO = new NoleggioDAO();
	public static OptionalDAO optionalDAO = new OptionalDAO();
	public static TrasferimentoDAO trasferimentoDAO = new TrasferimentoDAO();

	/**
	 * Viene recuperata l'agenzia locale
	 */
	public AgenziaServlet() {
		thisAgenzia = agenziaDAO.getLocale();
	}

	/*
	 * controlla la validità di username e password, se sono validi restituisce
	 * il dipendente associato. altrimenti ritorna null
	 */
	public Dipendente login(String user, String pass) {
		login = dipendenteDAO.checkUserPassDipendente(user, pass);
		if (login) {
			Dipendente logging = dipendenteDAO.getDipendente(user);
			HttpSession session = getThreadLocalRequest().getSession(true);
			logging.setSessionId(session.getId());
			session.setAttribute("username", logging.getUser());
			return logging;
		} else
			return null;
	}

	/*
	 * Controlla se vi è una sessione attiva se è attiva ritorna il dipendente
	 * loggato, altrimenti ritorna null
	 */
	public Dipendente loginSessione() {
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session == null)
			return null;
		String logging = (String) session.getAttribute("username");
		Dipendente dip = dipendenteDAO.getDipendente(logging);
		if (dip.getSessionId() == session.getId()) {
			return dip;
		} else {
			return null;
		}
	}

	/*
	 * effettua il logout (invalidando la sessione)
	 */
	public void logOut() {
		HttpSession session = getThreadLocalRequest().getSession(false);
		session.invalidate();
	}

	/*
	 * registra un nuovo cliente
	 */
	public Cliente registraCliente(String nome, String cognome,
			String indirizzo, String patente) {
		Cliente nuovo = new Cliente(nome, cognome, indirizzo, patente);
		clienteDAO.add(nuovo);
		return nuovo;
	}

	/*
	 * crea un nuovo noleggio con cliente, auto, inizio, fine, optional,
	 * guidatori, destinazione specificati
	 */
	public Noleggio nuovoNoleggio(Cliente cliente, Auto car, Date start,
			Date end, Agenzia dest, List<Optional> optional,
			List<Cliente> guidatori) {
		car = autoDAO.getAuto(car.getTarga());
		Noleggio creato;
		try {
			creato = new Noleggio(car, cliente, start, end, dest, thisAgenzia);
		} catch (ParseException e) {
			creato = null;
			return creato;
		}
		if (guidatori != null) {
			creato.setGuidatoriAggiuntivi(guidatori);
		}
		if (optional != null) {
			creato.setOptional(optional);
			for(Optional opt: optional)
			{
				System.out.println(opt.getTipo());
			}
		}

		if (dest.getName().equals(thisAgenzia.getName())) {
			noleggioDAO.add(creato);
		} else {
			//manda il noleggio all'agenzia remota
			remoteNoleggioStart(creato);
			
			// rimuovo clienti, optional e auto
			autoDAO.remove(car);
			clienteDAO.remove(cliente);
			if (optional != null) {
				optionalDAO.removeAllList(optional);
			}
			if (guidatori != null) {
				clienteDAO.removeAllList(guidatori);
			}
		}
		return creato;
	}

	/**
	 * manda un noleggio remoto alla destinazione remota
	 * 
	 * @param creato
	 *            il noleggio da mandare all'agenzia remota
	 */
	private void remoteNoleggioStart(Noleggio creato) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI agRemota;
		try {
			agRemota = new URI(creato.getAgenziaArrivo().getRestUri());
			WebResource service = client.resource(agRemota);
			service.path("/noleggioremoto/store")
					.accept(MediaType.APPLICATION_XML).post(creato);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Registra la fine del noleggio
	 */
	public void fineNoleggio(Noleggio n) {
		Auto auto = n.getAuto();
		Cliente cli = n.getCliente();


		for (Cliente guid : n.getGuidatoriAggiuntivi()) {
			if (!clienteDAO.containsCliente(guid.getNumeroPatente())) {
				clienteDAO.add(guid);
			}
		}
		if (!autoDAO.containsAuto(auto.getTarga()))
			autoDAO.add(auto);
		if (!clienteDAO.containsCliente(cli.getNumeroPatente())) {
			clienteDAO.add(cli);
		}
		if (!n.getAgenziaPartenza().getName().equals(thisAgenzia.getName())) {
			restoreClient(n.getCliente(), n.getAgenziaPartenza());
			for (Cliente guid : n.getGuidatoriAggiuntivi()) {
				restoreClient(guid, n.getAgenziaPartenza());
			}
		}
		noleggioDAO.remove(n);
	}

	/**
	 * Ripristina il cliente rimosso dall'agenzia di partenza
	 * 
	 * @param cliente
	 *            il cliente da ripristinare
	 * @param agenziaPartenza
	 *            l'agenzia a cui mandarlo
	 */
	private void restoreClient(Cliente cliente, Agenzia agenziaPartenza) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI agRemota;
		try {
			agRemota = new URI(agenziaPartenza.getRestUri());
			WebResource service = client.resource(agRemota);
			service.path("/noleggioremoto/restorecliente")
					.accept(MediaType.APPLICATION_XML).post(cliente);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public List<Auto> listaAuto() {
		return autoDAO.getAll();
	}

	/*
	 * Restituisce i noleggi riconsegnati per il dipendente del front office,
	 * non riconsegnati per l'addetto al rifornimento/trasferimento
	 */
	public List<Noleggio> listaNoleggi(Dipendente dip) {
		return (ArrayList<Noleggio>) noleggioDAO.getAllNoleggi(dip);
	}

	public List<Agenzia> listaAgenzia() {
		return agenziaDAO.getAll();
	}

	@Override
	public Auto registraAuto(String modello, String targa, int tipo) {
		Auto nuovo = new Auto(modello, targa, tipo);
		autoDAO.add(nuovo);
		return nuovo;
	}

	public List<Cliente> listaClienti() {
		return clienteDAO.getAll();
	}

	@Override
	public boolean rimuoviCliente(Cliente cliente) {
		if (!noleggioDAO.checkPendingNoleggi(cliente)) {
			clienteDAO.remove(cliente);
			return true;
		} else
			return false;
	}

	/*
	 * Registra la riconsegna dell'auto del noleggio con id specificato
	 */
	public void registraRiconsegna(long id, boolean riconsegna) {
		noleggioDAO.registerRiconsegna(id, riconsegna);
	}

	public List<Optional> getAllOptional() {
		return optionalDAO.getAll();
	}

	/*
	 * Avvia un trasferimento (quando è già stato preso in carico da un addetto)
	 */
	public Boolean avvioTrasferimento(Agenzia target,
			Trasferimento trasferimento) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI agRemota;

		try {
			agRemota = new URI(target.getRestUri());
			WebResource service = client.resource(agRemota)
					.path("/trasf/store");
			service.post(trasferimento);
			return true;
		} catch (Exception exc) {
			return false;
		}
	}

	/*
	 * Richede un auto all'agenzia specificata
	 */
	public Boolean richiestaAuto(Agenzia target, Auto richiesta) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI agRemota;
		Trasferimento trasf = new Trasferimento(target, thisAgenzia,
				richiesta.getTarga(), richiesta.getModello(),
				richiesta.getTipo());
		try {
			agRemota = new URI(target.getRestUri());
			WebResource service = client.resource(agRemota).path(
					"/trasf/request");
			service.post(trasf);
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	/*
	 * Recupera la lista di auto disponibili dall'agenzia specificata
	 */
	public List<Auto> elencoAuto(Agenzia target) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI agRemota;
		try {
			agRemota = new URI(target.getRestUri());
			WebResource service = client.resource(agRemota).path(
					"/trasf/getallauto");
			List<Auto> result = service.accept(MediaType.APPLICATION_XML)
					.accept(MediaType.TEXT_PLAIN)
					.get(new GenericType<List<Auto>>() {
					});
			return result;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Trasferimento> getTrasferimentiPartenza() {
		return trasferimentoDAO.getTrasferimenti("partenza");
	}

	public List<Trasferimento> getTrasferimentiArrivo() {
		return trasferimentoDAO.getTrasferimenti("destinazione");
	}

	public void rimuoviTrasferimento(Trasferimento trasf) {
		trasferimentoDAO.remove(trasf);
	}

	public void terminaTrasferimento(Trasferimento trasf) {
		Auto auto = new Auto(trasf.getModelloAuto(), trasf.getTargaAuto(),
				trasf.getTipoAuto());
		trasferimentoDAO.remove(trasf);
		autoDAO.add(auto);
	}

}
