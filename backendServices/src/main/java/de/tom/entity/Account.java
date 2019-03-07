package de.tom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "account")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String name;

	private String password;

	private String role;

	private Instant created;

	protected Account() {}

	public Account( final String name, final String password, final String role) {
		this.name = name;
		this.password = password;
		this.role = role;
		this.created = Instant.now();
	}

	public Long getId() {
		return id;
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

	public String getRole() {
		return role;
	}

	public void setRole( String role ) {
		this.role = role;
	}

	public Instant getCreated() {
		return created;
	}

	@JsonIgnore
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	private static String getDatetimeString(final Instant instant) {
		if( null == instant ) {
			return "null";
		}
		return instant.atZone( ZoneId.systemDefault() ).format( formatter );
	}

	@Override
	public String toString() {
		return "Account{" +
			"id=" + id +
			", name='" + name +
			"', password=<protected>" +
			", role='" + role +
			"', created=" + getDatetimeString(created) +
			'}';
	}
}
