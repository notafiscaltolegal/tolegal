package gov.to.dto;

public class RespostaReceitaFederalDTO {

	private String nomePessoaFisica;
	private String mensagemErro;
	
	public String getNomePessoaFisica() {
		return nomePessoaFisica;
	}
	public void setNomePessoaFisica(String nomePessoaFisica) {
		this.nomePessoaFisica = nomePessoaFisica;
	}
	public String getMensagemErro() {
		return mensagemErro;
	}
	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
}