package com.project.ronf.shared.entities;

import javax.persistence.Entity;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.project.ronf.shared.BCrypt;

@Entity
public class DipTrasferimento extends Dipendente implements IsSerializable {

	public DipTrasferimento() {

	}

	public DipTrasferimento(String us, String pwd, Agenzia ag) {
		user = us;
		pass = BCrypt.hashpw(pwd, BCrypt.gensalt());
		agenzia = ag;
	}
}
