package br.net.neuromancer.bigpolis.tserd14.models;

public enum PolCargo {
	PRESIDENTE("Presidente"),
	VICE_PRESIDENTE("Vice-Presidente"),
	GOVERNADOR("Governador"),
	VICE_GOVERNADOR("Vice-Governador"),
	SENADOR("Senador"),
	PRIMEIRO_SUPLENTE_SENADOR("Primeiro Suplente Senador"),
	SEGUNDO_SUPLENTE_SENADOR("Segundo Suplente Senador"),
	DEPUTADO_FEDERAL("Deputado Federal"),
	DEPUTADO_ESTADUAL("Deputado Estadual"),
	DEPUTADO_DISTRITAL("Deputado Distrital");
	
	private final String cargo;
    
	PolCargo(String cg){
		cargo = cg;
	}

	public String getCargo() {
		return cargo;
	}

}
