package gov.to.filtro;

import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroMensagemVisuCidadaoToLegalDTO {
	
	@EntityProperty(value="id")
	private Long id;
	
	@EntityProperty(value="dataLeitura")
	private DataFiltroBetween dataLeitura;
	
	@EntityProperty(value="titulo")
	private String titulo;
	
	@EntityProperty(value="mensagem")
	private String mensagem;
	
	@EntityProperty(value="cpf")
	private String cpf;
	
	public FiltroMensagemVisuCidadaoToLegalDTO() {
		dataLeitura = new DataFiltroBetween();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DataFiltroBetween getDataLeitura() {
		return dataLeitura;
	}

	public void setDataLeitura(DataFiltroBetween dataLeitura) {
		this.dataLeitura = dataLeitura;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}