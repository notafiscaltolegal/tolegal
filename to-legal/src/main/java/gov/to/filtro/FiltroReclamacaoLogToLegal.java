package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroReclamacaoLogToLegal implements Filtro {

	@EntityProperty("reclamacaoToLegal.id")
	private Long idReclamacao;

	public Long getIdReclamacao() {
		return idReclamacao;
	}

	public void setIdReclamacao(Long idReclamacao) {
		this.idReclamacao = idReclamacao;
	}
}