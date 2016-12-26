package gov.goias.entidades;

import java.io.Serializable;

public class GENUf implements Serializable{
	
	private static final long serialVersionUID = 655593573661615913L;

	private String codgUf;
	
	private String nomeUf;
	
	private Integer codgIbge;

	public GENUf() {
		;
	}
	
	public String getCodgUf() {
		return codgUf;
	}

	public void setCodgUf(String codgUf) {
		this.codgUf = codgUf;
	}

	public String getNomeUf() {
		return nomeUf;
	}

	public void setNomeUf(String nomeUf) {
		this.nomeUf = nomeUf;
	}

	public Integer getCodgIbge() {
		return codgIbge;
	}

	public void setCodgIbge(Integer codgIbge) {
		this.codgIbge = codgIbge;
	}
	
}
