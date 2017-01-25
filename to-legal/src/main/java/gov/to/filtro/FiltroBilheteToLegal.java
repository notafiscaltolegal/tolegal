package gov.to.filtro;

import gov.to.dominio.SituacaoBilhete;
import gov.to.persistencia.EntityProperty;

public class FiltroBilheteToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty("sorteioToLegal.id")
	private Long idSorteio;
	
	@EntityProperty("stBilhete")
	private SituacaoBilhete situacaoBilhete;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getIdSorteio() {
		return idSorteio;
	}

	public void setIdSorteio(Long idSorteio) {
		this.idSorteio = idSorteio;
	}

	public SituacaoBilhete getSituacaoBilhete() {
		return situacaoBilhete;
	}

	public void setSituacaoBilhete(SituacaoBilhete situacaoBilhete) {
		this.situacaoBilhete = situacaoBilhete;
	}
}