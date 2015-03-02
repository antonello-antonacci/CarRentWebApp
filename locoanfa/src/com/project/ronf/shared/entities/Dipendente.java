package com.project.ronf.shared.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.project.ronf.shared.BCrypt;

@Entity
public class Dipendente implements IsSerializable {
	@Id
	@GeneratedValue
	public long id;

	@Column(unique = true)
	String user;

	String pass;
	String sessionId;

	@OneToOne
	public Agenzia agenzia;

	public Dipendente() {
		user = null;
		pass = null;
	}

	public Long getId() {
		return id;
	}

	public Dipendente(String us, String pwd, Agenzia ag) {
		agenzia = ag;
		user = us;
		pass = BCrypt.hashpw(pwd, BCrypt.gensalt());
	}

	public Boolean checkPassword(String pwd) {
		return BCrypt.checkpw(pwd, pass);
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = BCrypt.hashpw(pass, BCrypt.gensalt());
	}

	public String getUser() {
		return user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setAgenzia(Agenzia agenzia) {
		this.agenzia = agenzia;
	}

	public Agenzia getAgenzia() {
		return agenzia;
	}

}
