package br.net.neuromancer.bigpolis.tserd14.models;

public enum GeoUF {
	AC("AC - ACRE"),
	AL("AL - ALAGOAS"),
	AM("AM - AMAZONAS"),
	AP("AP - AMAPÁ"),
	BA("BA - BAHIA"),
	BR("BR - BRASIL"),
	CE("CE - CEARÁ"),
	DF("DF - DISTRITO FEDERAL"),
	ES("ES - ESPÍRITO SANTO"),
	GO("GO - GOIÁS"),
	MA("MA - MARANHÃO"),
	MG("MG - MINAS GERAIS"),
	MS("MS - MATO GROSSO DO SUL"),
	MT("MT - MATO GROSSO"),
	PA("PA - PARÁ"),
	PB("PB - PARAÍBA"),
	PE("PE - PERNAMBUCO"),
	PI("PI - PIAUÍ"),
	PR("PR - PARANÁ"),
	RJ("RJ - RIO DE JANEIRO"),
	RN("RN - RIO GRANDE DO NORTE"),
	RO("RO - RONDÔNIA"),
	RR("RR - RORAIMA"),
	RS("RS - RIO GRANDE DO SUL"),
	SC("SC - SANTA CATARINA"),
	SE("SE - SERGIPE"),
	SP("SP - SÃO PAULO"),
	TO("TO - TOCANTINS");
	
    private final String name;
    
	GeoUF(String nm){
		name = nm;
	}


	public String getName() {
		return name;
	}
}
