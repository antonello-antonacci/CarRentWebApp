package com.project.ronf.shared.entities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "noleggio")
@XmlAccessorType(XmlAccessType.FIELD)
public class Noleggio implements IsSerializable {

	@Id
	@GeneratedValue
	Long id;

	@OneToOne
	@XmlElement(name = "cliente")
	Cliente cliente;

	@OneToOne
	@XmlElement(name = "auto")
	Auto auto;

	@OneToOne
	Agenzia agenziaArrivo;

	@OneToOne
	Agenzia agenziaPartenza;

	@OneToMany(fetch = FetchType.EAGER)
	@XmlElement(name = "guidatoriAggiuntivi")
	List<Cliente> guidatoriAggiuntivi = new ArrayList<Cliente>();

	@OneToMany(fetch = FetchType.EAGER)
	@XmlElement(name = "optionals")
	List<Optional> optional = new ArrayList<Optional>();

	public static final int PREZZO_RIFORNIMENTO = 50;
	
	long partenza, arrivo;
	Boolean riconsegna = false;
	Boolean rifornimento = false;

	long giorni;

	public Noleggio() {
	}

	public Noleggio(Auto car, Cliente client, Date start, Date end,
			Agenzia destination, Agenzia source) throws ParseException {
		cliente = client;
		auto = car;
		partenza = start.getTime();
		arrivo = end.getTime();
		agenziaArrivo = destination;
		agenziaPartenza = source;
		giorni = (long) (Math.ceil((arrivo - partenza) / 86400000.0)) + 1;
	}

	public float getPrice() {
		float prezzo = Auto.prezzo[auto.tipo];
		float totale = ((giorni) * prezzo);
		if (rifornimento)
			totale += 50;
		int noOptional = guidatoriAggiuntivi.size() + optional.size();
		totale += (20 * noOptional);
		return totale;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Auto getAuto() {
		return auto;
	}

	public Agenzia getAgenziaArrivo() {
		return agenziaArrivo;
	}

	public Agenzia getAgenziaPartenza() {
		return agenziaPartenza;
	}

	public List<Cliente> getGuidatoriAggiuntivi() {
		return guidatoriAggiuntivi;
	}

	public void setGuidatoriAggiuntivi(List<Cliente> guidatori) {
		guidatoriAggiuntivi = guidatori;
	}

	public long getPartenza() {
		return partenza;
	}

	public long getArrivo() {
		return arrivo;
	}

	public Boolean getRiconsegna() {
		return riconsegna;
	}

	public void setRiconsegna(Boolean riconsegna) {
		this.riconsegna = riconsegna;
	}

	public Boolean getRifornimento() {
		return rifornimento;
	}

	public void setRifornimento(Boolean rifornimento) {
		this.rifornimento = rifornimento;
	}

	public Long getId() {
		return id;
	}

	public long getGiorni() {
		return giorni;
	}

	public void setCliente(Cliente cli) {
		this.cliente = cli;
	}

	public void setAgenziaPartenza(Agenzia part) {
		this.agenziaPartenza = part;
	}

	public void setAgenziaArrivo(Agenzia dest) {
		this.agenziaArrivo = dest;
	}

	public List<Optional> getOptional() {
		return this.optional;
	}

	public void setOptional(List<Optional> optionalToAdd) {
		this.optional = optionalToAdd;
	}

}
