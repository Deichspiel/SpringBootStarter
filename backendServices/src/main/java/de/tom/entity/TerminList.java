package de.tom.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TerminList implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Termin> termine;

	public TerminList() {
		termine = new ArrayList<>();
	}

	public TerminList( Iterable<Termin> termine) {
		this.termine = new ArrayList<>();
		if( null != termine ) {
			termine.forEach( this.termine::add );
		}
	}

	public List<Termin> getTermine() {
		return new ArrayList<>(termine);
	}

	public void setTermine( List<Termin> termine ) {
		this.termine = new ArrayList<>(termine);
	}
}
