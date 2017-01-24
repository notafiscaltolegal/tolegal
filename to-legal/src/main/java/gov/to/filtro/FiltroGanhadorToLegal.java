package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroGanhadorToLegal {

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty("numeroSorteio")
	private Integer numeroSorteio;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Integer getNumeroSorteio() {
		return numeroSorteio;
	}

	public void setNumeroSorteio(Integer numeroSorteio) {
		this.numeroSorteio = numeroSorteio;
	}
}