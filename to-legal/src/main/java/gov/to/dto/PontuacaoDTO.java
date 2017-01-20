package gov.to.dto;

import java.util.Date;

public class PontuacaoDTO {
	
	private String cnpj;
	private String estabelecimento;
	private String numero;
	private Date emissao;
	private String registro;
	private Double valor;
	private Integer qtdePontos;
	private String status;
	private String detalhe;
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getEstabelecimento() {
		return estabelecimento;
	}
	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getEmissao() {
		return emissao;
	}
	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}
	public String getRegistro() {
		return registro;
	}
	public void setRegistro(String registro) {
		this.registro = registro;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Integer getQtdePontos() {
		return qtdePontos;
	}
	public void setQtdePontos(Integer qtdePontos) {
		this.qtdePontos = qtdePontos;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}
}
