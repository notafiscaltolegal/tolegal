package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroContribuinteToLegal implements Filtro{

	@EntityProperty("cnpj")
	private String cnpj;
	
	@EntityProperty("id")
	private String inscricaoEstadual;
	
	@EntityProperty("razaoSocial")
	private String razaoSocial;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
}