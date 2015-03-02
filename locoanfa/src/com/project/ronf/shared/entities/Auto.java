package com.project.ronf.shared.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlTransient;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "auto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Auto implements IsSerializable {

	public static final int IND_MINI = 0;
	public static final int IND_FAMILY = 1;
	public static final int IND_SPORT = 2;
	public static final int IND_PRESTIGE = 3;
	public static final int IND_EPOCA = 4;

	public static String[] tipi = { "mini", "family", "sport", "prestige",
			"auto d'epoca" };
	public static float[] prezzo = { 10, 20, 30, 40, 50 };

	@XmlAttribute
	String modello;

	@XmlAttribute
	int tipo;

	@Id
	@GeneratedValue
	@XmlTransient
	Long id;

	@XmlAttribute
	@Column(unique = true)
	String targa;

	public Auto() {
	}

	public Auto(String mod, String targ, int type) {
		modello = mod;
		targa = targ;
		tipo = type;
	}

	public String getModello() {
		return modello;
	}

	public int getTipo() {
		return tipo;
	}

	public Long getId() {
		return id;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

}
