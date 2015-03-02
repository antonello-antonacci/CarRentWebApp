package com.project.ronf.shared.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@Entity
@XmlRootElement(name = "agenzia")
public class Agenzia implements IsSerializable {

	String address;

	@Column(unique = true)
	String name;
	Boolean locale;

	@Column(unique = true)
	String restUri;

	@Id
	@GeneratedValue
	long id;

	public Agenzia() {

	}

	public Agenzia(String myaddr, String myname, Boolean isLocale, String url) {
		address = myaddr;
		name = myname;
		locale = isLocale;
		restUri = url;
	}

	public Boolean isLocale() {
		return locale;
	}

	public void setLocale(Boolean locale) {
		this.locale = locale;
	}

	public String getRestUri() {
		return restUri;
	}

	public void setRestUri(String restUri) {
		this.restUri = restUri;
	}

	public long getId() {
		return id;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}
}
