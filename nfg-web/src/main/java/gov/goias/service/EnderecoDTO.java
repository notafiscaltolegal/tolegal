package gov.goias.service;

import gov.goias.entidades.PessoaParticipante;

public class EnderecoDTO {
     
     private Integer cep;
     private String nomeLogradouro;
     private String numero;
     private String nomeBairro;
     private String complemento;
     
     private String nomeTipoLogradouro;
     private String nomeUf;
     private String nomeMunicipio;
     private String codgIbgeMunicipio;
     private Character enderecoHomolog;
     private PessoaParticipante cidadao;
     private String tipoLogradouro;
     private boolean participaSorteio;
     private boolean recebeEmail;
	private boolean cepInvalido;
     
	public Integer getCep() {
		return cep;
	}
	public void setCep(Integer cep) {
		this.cep = cep;
	}
	public String getNomeLogradouro() {
		return nomeLogradouro;
	}
	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNomeBairro() {
		return nomeBairro;
	}
	public void setNomeBairro(String nomeBairro) {
		this.nomeBairro = nomeBairro;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getNomeTipoLogradouro() {
		return nomeTipoLogradouro;
	}
	public void setNomeTipoLogradouro(String nomeTipoLogradouro) {
		this.nomeTipoLogradouro = nomeTipoLogradouro;
	}
	public String getNomeUf() {
		return nomeUf;
	}
	public void setNomeUf(String nomeUf) {
		this.nomeUf = nomeUf;
	}
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}
	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	public Character getEnderecoHomolog() {
		return enderecoHomolog;
	}
	public void setEnderecoHomolog(Character enderecoHomolog) {
		this.enderecoHomolog = enderecoHomolog;
	}
	public PessoaParticipante getCidadao() {
		return cidadao;
	}
	public void setCidadao(PessoaParticipante cidadao) {
		this.cidadao = cidadao;
	}
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	public boolean isParticipaSorteio() {
		return participaSorteio;
	}
	public void setParticipaSorteio(boolean participaSorteio) {
		this.participaSorteio = participaSorteio;
	}
	public boolean isRecebeEmail() {
		return recebeEmail;
	}
	public void setRecebeEmail(boolean recebeEmail) {
		this.recebeEmail = recebeEmail;
	}
	public boolean getCepInvalido() {
		return cepInvalido;
	}
	public void setCepInvalido(boolean cepInvalido) {
		this.cepInvalido = cepInvalido;
	}
	public String getCodgIbgeMunicipio() {
		return codgIbgeMunicipio;
	}
	public void setCodgIbgeMunicipio(String codgIbgeMunicipio) {
		this.codgIbgeMunicipio = codgIbgeMunicipio;
	}
}