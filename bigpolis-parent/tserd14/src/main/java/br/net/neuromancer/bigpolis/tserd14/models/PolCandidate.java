package br.net.neuromancer.bigpolis.tserd14.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PolCandidate implements Serializable{
	private Long sqCand;
	private String sgUe;
	private String name;
	private Integer numero;
	private String partido;
	
	public Long getSqCand() {
		return sqCand;
	}
	public void setSqCand(Long sqCand) {
		this.sqCand = sqCand;
	}
	public String getSgUe() {
		return sgUe;
	}
	public void setSgUe(String sgUe) {
		this.sgUe = sgUe;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getPartido() {
		return partido;
	}
	public void setPartido(String partido) {
		this.partido = partido;
	}


}
