package com.project.ronf.shared.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "optional")
public class Optional implements IsSerializable {

	@Id
	@GeneratedValue
	long id;

	int tipo;

	public static final int IND_SEGG = 0;
	public static final int IND_NAVIG = 1;

	public static final int PREZZO_SEGGIOLINO = 20;
	public static final int PREZZO_GUID_AGGIUNTIVO = 20;
	public static final int PREZZO_NAVIGATORE = 20;

	public static final String[] tipi = { "seggiolino auto", "navigatore" };

	public Optional() {
	}

	public Optional(int type) {
		tipo = type;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public long getId() {
		return id;
	}

}
