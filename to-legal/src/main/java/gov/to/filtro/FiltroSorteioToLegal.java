package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroSorteioToLegal implements Filtro{

	@EntityProperty("numeroSorteio")
	private Integer numeroSorteio;

	public Integer getNumeroSorteio() {
		return numeroSorteio;
	}

	public void setNumeroSorteio(Integer numeroSorteio) {
		this.numeroSorteio = numeroSorteio;
	}
}