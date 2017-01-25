package gov.to.filtro;

import gov.to.dominio.SituacaoMensagem;
import gov.to.persistencia.DataFiltroBetween;
import gov.to.persistencia.EntityProperty;

public class FiltroMensagemSefazToLegalDTO {
	
	@EntityProperty(value="id")
	private Long id;
	
	@EntityProperty(value="dataEnvio")
	private DataFiltroBetween dataEnvio;
	
	@EntityProperty(value="titulo")
	private String titulo;
	
	@EntityProperty(value="mensagem")
	private String mensagem;
	
	@EntityProperty(value="cpfAdmLogado")
	private String cpfAdmLogado;
	
	@EntityProperty(value="nomeAdmLogado")
	private String nomeAdmLogado;
	
	@EntityProperty(value="situacao")
	private SituacaoMensagem situacao;
	
	public FiltroMensagemSefazToLegalDTO() {
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

	public String getCpfAdmLogado() {
		return cpfAdmLogado;
	}

	public void setCpfAdmLogado(String cpfAdmLogado) {
		this.cpfAdmLogado = cpfAdmLogado;
	}

	public String getNomeAdmLogado() {
		return nomeAdmLogado;
	}

	public void setNomeAdmLogado(String nomeAdmLogado) {
		this.nomeAdmLogado = nomeAdmLogado;
	}

	public SituacaoMensagem getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoMensagem situacao) {
		this.situacao = situacao;
	}
}