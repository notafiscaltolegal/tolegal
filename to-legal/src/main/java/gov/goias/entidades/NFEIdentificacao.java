package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

public class NFEIdentificacao implements Serializable {

	private static final long serialVersionUID = 1049455724495796880L;

	private Long id;

	private Integer numeroNota;
	
	private String nomeEmpresa;
	
	private Date dataEmissao;
	
	private Double valorNota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Double getValorNota() {
		return valorNota;
	}

	public void setValorNota(Double valorNota) {
		this.valorNota = valorNota;
	}
}