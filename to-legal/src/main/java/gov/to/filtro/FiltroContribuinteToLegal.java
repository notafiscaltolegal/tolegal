package gov.to.filtro;

import gov.to.persistencia.EntityProperty;

public class FiltroContribuinteToLegal implements Filtro{

	@EntityProperty("cnpj")
	private String cnpj;
	
	@EntityProperty("id")
	private String inscricaoEstadual;
	
	@EntityProperty("razaoSocial")
	private String razaoSocial;
	
	@EntityProperty("senha")
	private String senha;
	
	public static String inscricaoEstadualFormat(Integer inscricaoEstaudal)
	{
		String ie = inscricaoEstaudal.toString();
		String part1 = ie.substring(0, 2);
		String part2 = ie.substring(2, 5);
		String part3 = ie.substring(5, 8);
		String part4 = ie.substring(8, 9);
		
		StringBuilder sb = new StringBuilder();
		sb.append(part1).append(".").append(part2).append(".").append(part3).append("-").append(part4);
		
		return sb.toString();
	}
	
	public static String cnpjlFormat(String cnpj)
	{
		String cnpjFormat = cnpj.toString();
		String part1 = cnpjFormat.substring(0, 2);
		String part2 = cnpjFormat.substring(2, 5);
		String part3 = cnpjFormat.substring(5, 8);
		String part4 = cnpjFormat.substring(8, 12);
		String part5 = cnpjFormat.substring(12, 14);
		
		StringBuilder sb = new StringBuilder();
		sb.append(part1).append(".").append(part2).append(".").append(part3).append("/").append(part4).append("-").append(part5);
		
		return sb.toString();
	}
	
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}