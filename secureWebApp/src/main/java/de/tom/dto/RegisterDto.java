package de.tom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisterDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String password;
	private String password1;

	public RegisterDto() {}

	public RegisterDto( String name, String password, String password1 ) {
		this.name = name;
		this.password = password;
		this.password1 = password1;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1( String password1 ) {
		this.password1 = password1;
	}

	@Override
	public String toString() {
		return "RegisterDto{" +
			"name='" + name +
			"', password=<protected>" +
			", password1=<protected>" +
			", equals=" + Objects.equals( password, password1 ) +
			'}';
	}
}
