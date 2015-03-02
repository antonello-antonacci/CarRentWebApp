package com.project.ronf.shared.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "trasferimento")
@XmlAccessorType(XmlAccessType.FIELD)
public class Trasferimento implements IsSerializable {

	@Id
	@GeneratedValue
	long id;

	@OneToOne
	Agenzia destinazione;

	@OneToOne
	Agenzia partenza;

	String targaAuto, modelloAuto;
	int tipoAuto;

	public Trasferimento() {
	}

	public Trasferimento(Agenzia from, Agenzia to, String targa,
			String modello, int tipo) {
		partenza = from;
		destinazione = to;
		targaAuto = targa;
		modelloAuto = modello;
		tipoAuto = tipo;
	}

	public long getId() {
		return id;
	}

	public Agenzia getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(Agenzia dest) {
		this.destinazione = dest;
	}

	public Agenzia getPartenza() {
		return partenza;
	}

	public String getTargaAuto() {
		return targaAuto;
	}

	public String getModelloAuto() {
		return modelloAuto;
	}

	public int getTipoAuto() {
		return tipoAuto;
	}

	public void setPartenza(Agenzia part) {
		this.partenza = part;
	}

}
