package de.tom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "termin")
public class Termin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty(message = "{termin.resource.notempty")
	private String resource;

	private String user;

	private LocalDateTime start = LocalDateTime.now();

	private LocalDateTime end = start.plusHours(1L);

	public Termin() {}

	public Termin( String resource, String user, LocalDateTime start, LocalDateTime end ) {
		this.resource = resource;
		this.user = user;
		this.start = start;
		this.end = end;
	}

	public Long getId() {
		return id;
	}

	public String getResource() {
		return resource;
	}

	public void setResource( String resource ) {
		this.resource = resource;
	}

	public String getUser() {
		return user;
	}

	public void setUser( String user ) {
		this.user = user;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart( LocalDateTime start ) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd( LocalDateTime end ) {
		this.end = end;
	}

	@JsonIgnore
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	private static String getDatetimeString(final LocalDateTime dateTime) {
		if( null == dateTime ) {
			return "null";
		}
		return dateTime.format( formatter );
	}

	@Override
	public String toString() {
		return "Termin{" +
			"id=" + id +
			", resource='" + resource +
			"', user='" + user +
			"', start=" + getDatetimeString(start) +
			", end=" + getDatetimeString(end) +
			'}';
	}
}
