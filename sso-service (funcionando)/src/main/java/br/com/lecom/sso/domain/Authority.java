package br.com.lecom.sso.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

@Entity
public class Authority {

	@Id
	@NotNull
	@Size(min = 0, max = 50)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
