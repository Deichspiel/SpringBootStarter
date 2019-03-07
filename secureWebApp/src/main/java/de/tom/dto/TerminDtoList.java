package de.tom.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TerminDtoList implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<TerminDto> termine;

	public TerminDtoList() {
		termine = new ArrayList<>();
	}

	public TerminDtoList(List<TerminDto> termine) {
		this.termine = new ArrayList<>(termine);
	}

	public List<TerminDto> getTermine() {
		return new ArrayList<>(termine);
	}

	public void setTermine( List<TerminDto> termine ) {
		this.termine = new ArrayList<>(termine);
	}
}
