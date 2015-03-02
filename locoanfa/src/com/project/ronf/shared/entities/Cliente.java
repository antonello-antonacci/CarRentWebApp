package com.project.ronf.shared.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "cliente")
public class Cliente implements IsSerializable {

	@Id
	@GeneratedValue
	long id;
	String nome;
	String cognome;
	String indirizzo;

	@Column(unique = true)
	String numeroPatente;

	public Cliente() {
	}

	public Cliente(String name, String surname, String address, String patentNo) {
		nome = name;
		cognome = surname;
		indirizzo = address;
		numeroPatente = patentNo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String surname) {
		this.cognome = surname;
	}

	public String getNumeroPatente() {
		return numeroPatente;
	}

	public void setNumeroPatente(String noPatente) {
		this.numeroPatente = noPatente;
	}

}
