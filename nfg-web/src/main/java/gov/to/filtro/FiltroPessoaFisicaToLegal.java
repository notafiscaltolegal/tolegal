package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroPessoaFisicaToLegal implements Filtro{

	@EntityProperty("cpf")
	private String cpf;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}