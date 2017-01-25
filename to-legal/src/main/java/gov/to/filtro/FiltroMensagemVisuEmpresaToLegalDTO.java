package gov.to.filtro;

import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroMensagemVisuEmpresaToLegalDTO {
	
	@EntityProperty(value="id")
	private Long id;
	
	@EntityProperty(value="dataEnvio")
	private DataFiltroBetween dataEnvio;
	
	@EntityProperty(value="titulo")
	private String titulo;
	
	@EntityProperty(value="mensagem")
	private String mensagem;
	
	@EntityProperty(value="inscricaoEstadual")
	private String inscricaoEstadual;
	
	public FiltroMensagemVisuEmpresaToLegalDTO() {
		dataEnvio = new DataFiltroBetween();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DataFiltroBetween getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(DataFiltroBetween dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
}