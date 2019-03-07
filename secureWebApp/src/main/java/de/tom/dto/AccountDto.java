package de.tom.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private String name;
	private String password;
	private String role;
	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm:ss")
	private Instant created;

	public AccountDto() {}

	public AccountDto(final String name, final String password, final String role) {
		this.name = name;
		this.password = password;
		this.role = role;
		this.created = Instant.now();
	}

	public AccountDto( final RegisterDto register ) {
		this( register.getName(), register.getPassword(), "ROLE_USER" );
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public void setCreated(Instant created) {
		this.created = created;
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
		return "AccountDto{" +
			"id=" + id +
			", name='" + name +
			"', password=<protected>" +
			", role='" + role +
			"', created=" + getDatetimeString(created) +
			'}';
	}
}
