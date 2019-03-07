package de.tom.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TerminDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id = 0L;
	private String resource;
	private String user;
	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm")
	private LocalDateTime start = LocalDateTime.now();
	@DateTimeFormat(pattern="dd.MM.yyyy HH:mm")
	private LocalDateTime end = start.plusHours(1L);

	public TerminDto() {}

	public TerminDto( Long id, String resource, String user, LocalDateTime start, LocalDateTime end ) {
		this.id = id;
		this.resource = resource;
		this.user = user;
		this.start = start;
		this.end = end;
	}

	public Long getId() {
		return id;
	}
	public void setId( Long id ) {
		this.id = id;
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
		return "TerminDto{" +
			"id=" + id +
			", resource='" + resource +
			"', user='" + user +
			"', start=" + getDatetimeString(start) +
			", end=" + getDatetimeString(end) +
			'}';
	}
}
