package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroBilheteToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;
	
	@EntityProperty("sorteioToLegal.id")
	private Long idSorteio;

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
}