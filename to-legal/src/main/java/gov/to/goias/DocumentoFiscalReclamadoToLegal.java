package gov.to.goias;

import java.sql.Blob;
import java.util.Date;

import gov.goias.entidades.PessoaParticipante;

public class DocumentoFiscalReclamadoToLegal {

	private Integer id;

	private Date dataDocumentoFiscal;

	private Integer numero;

	private Integer tipoDocumentoFiscal;

	private Double valor;

	private Date dataReclamacao;

	private String reclamacaoResolvida;

	private Blob imgDocumentoFiscal;

	private Integer tipoExtensao;

	private String motivoReclamacao;

	private PessoaParticipante pessoaParticipante;

	private String numeroCnpjEmpresa;

	private String nomeFantasiaEmpresa;

	
	private String listaAndamentoStr;

	
	private String statusAndamentoStr;

	
	private String disableRadioBtn;

	
	private String razaoSocial;

	
	private String inscricaoEmpresa;
	
	private String nome;

	
	private String descUltimaSituacao;

	private Integer codgUltimaSituacao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataDocumentoFiscal() {
		return dataDocumentoFiscal;
	}

	public void setDataDocumentoFiscal(Date dataDocumentoFiscal) {
		this.dataDocumentoFiscal = dataDocumentoFiscal;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getTipoDocumentoFiscal() {
		return tipoDocumentoFiscal;
	}

	public void setTipoDocumentoFiscal(Integer tipoDocumentoFiscal) {
		this.tipoDocumentoFiscal = tipoDocumentoFiscal;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataReclamacao() {
		return dataReclamacao;
	}

	public void setDataReclamacao(Date dataReclamacao) {
		this.dataReclamacao = dataReclamacao;
	}

	public String getReclamacaoResolvida() {
		return reclamacaoResolvida;
	}

	public void setReclamacaoResolvida(String reclamacaoResolvida) {
		this.reclamacaoResolvida = reclamacaoResolvida;
	}

	public Blob getImgDocumentoFiscal() {
		return imgDocumentoFiscal;
	}

	public void setImgDocumentoFiscal(Blob imgDocumentoFiscal) {
		this.imgDocumentoFiscal = imgDocumentoFiscal;
	}

	public Integer getTipoExtensao() {
		return tipoExtensao;
	}

	public void setTipoExtensao(Integer tipoExtensao) {
		this.tipoExtensao = tipoExtensao;
	}

	public String getMotivoReclamacao() {
		return motivoReclamacao;
	}

	public void setMotivoReclamacao(String motivoReclamacao) {
		this.motivoReclamacao = motivoReclamacao;
	}

	public PessoaParticipante getPessoaParticipante() {
		return pessoaParticipante;
	}

	public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
		this.pessoaParticipante = pessoaParticipante;
	}

	public String getNumeroCnpjEmpresa() {
		return numeroCnpjEmpresa;
	}

	public void setNumeroCnpjEmpresa(String numeroCnpjEmpresa) {
		this.numeroCnpjEmpresa = numeroCnpjEmpresa;
	}

	public String getNomeFantasiaEmpresa() {
		return nomeFantasiaEmpresa;
	}

	public void setNomeFantasiaEmpresa(String nomeFantasiaEmpresa) {
		this.nomeFantasiaEmpresa = nomeFantasiaEmpresa;
	}

	public String getListaAndamentoStr() {
		return listaAndamentoStr;
	}

	public void setListaAndamentoStr(String listaAndamentoStr) {
		this.listaAndamentoStr = listaAndamentoStr;
	}

	public String getStatusAndamentoStr() {
		return statusAndamentoStr;
	}

	public void setStatusAndamentoStr(String statusAndamentoStr) {
		this.statusAndamentoStr = statusAndamentoStr;
	}

	public String getDisableRadioBtn() {
		return disableRadioBtn;
	}

	public void setDisableRadioBtn(String disableRadioBtn) {
		this.disableRadioBtn = disableRadioBtn;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getInscricaoEmpresa() {
		return inscricaoEmpresa;
	}

	public void setInscricaoEmpresa(String inscricaoEmpresa) {
		this.inscricaoEmpresa = inscricaoEmpresa;
	}

	public String getDescUltimaSituacao() {
		return descUltimaSituacao;
	}

	public void setDescUltimaSituacao(String descUltimaSituacao) {
		this.descUltimaSituacao = descUltimaSituacao;
	}

	public Integer getCodgUltimaSituacao() {
		return codgUltimaSituacao;
	}

	public void setCodgUltimaSituacao(Integer codgUltimaSituacao) {
		this.codgUltimaSituacao = codgUltimaSituacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
