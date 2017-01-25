package gov.to.filtro;

import gov.to.dominio.SituacaoSorteio;
import gov.to.persistencia.EntityProperty;

public class FiltroSorteioToLegal implements Filtro{

	@EntityProperty("numeroSorteio")
	private Integer numeroSorteio;
	
	@EntityProperty("situacao")
	private SituacaoSorteio situacaoSorteio;
	
	public Integer getNumeroSorteio() {
		return numeroSorteio;
	}

	public void setNumeroSorteio(Integer numeroSorteio) {
		this.numeroSorteio = numeroSorteio;
	}

	public SituacaoSorteio getSituacaoSorteio() {
		return situacaoSorteio;
	}

	public void setSituacaoSorteio(SituacaoSorteio situacaoSorteio) {
		this.situacaoSorteio = situacaoSorteio;
	}
}