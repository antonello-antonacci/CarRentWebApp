package com.project.ronf.shared.entities;

import javax.persistence.Entity;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.project.ronf.shared.BCrypt;

@Entity
public class DipFrontOffice extends Dipendente implements IsSerializable {

	public DipFrontOffice() {

	}

	public DipFrontOffice(String us, String pwd, Agenzia ag) {
		user = us;
		pass = BCrypt.hashpw(pwd, BCrypt.gensalt());
		agenzia = ag;
	}
}
