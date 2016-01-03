package br.net.neuromancer.bigpolis.tserd14.requests;

import java.io.Serializable;

import br.net.neuromancer.bigpolis.tserd14.models.GeoUF;
import br.net.neuromancer.bigpolis.tserd14.models.PolCargo;

@SuppressWarnings("serial")
public class CandidatesByRegionByOfficeCrawlRequest  implements Serializable{

	public CandidatesByRegionByOfficeCrawlRequest(GeoUF uf, PolCargo cargo) {
		super();
		this.uf = uf;
		this.cargo = cargo;
	}
	
	public GeoUF getUf() {
		return uf;
	}
	public void setUf(GeoUF uf) {
		this.uf = uf;
	}
	public PolCargo getCargo() {
		return cargo;
	}
	public void setCargo(PolCargo cargo) {
		this.cargo = cargo;
	}
	
	private GeoUF uf;
	private PolCargo cargo;
	
	@Override
	public String toString() {
		return "CandidatesByRegionByOfficeCrawlRequest [uf=" + uf + ", cargo=" + cargo + "]";
	}
	
	
}
