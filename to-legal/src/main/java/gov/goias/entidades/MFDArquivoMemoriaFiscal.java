package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bruno-cff on 01/07/2015.
 */
public class MFDArquivoMemoriaFiscal implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4058380759243929096L;

    private Integer id;

    private String nome;

    private Integer codigo;

    private Integer tipoFinalidade;

    private Integer referencia;

    private Date dataEntrega;

    private Integer numeroInscricao;

    private Date dataInicioApuracao;

    private Date dataFimApuracao;

    private Integer quantidadeLinha;

    private Date dataCatalogacao;

    private Date dataInicioProcessamento;

    private Date dataFimProcessamento;

    private String statusProcessamento;

    private String infoContexto;

    private String infoDiretorio;

    private String tipoModelo;

    private String infoMensagemLog;

    private Date dataEntregaSefaz;

    private String infoHash;

    private String cpfContribuinte;

    private String cnpjEstabelecimento;

    private String versaoSoftware;

    private Date dataGravacaoSoftware;

    private Integer numeroSequencial;

    private String tipoComando;

    private Integer reducaoZInicio;

    private Integer reducaoZFim;

    private String versaoBiblioteca;

    private String versaoAtoCotepe;

    private Integer numeroOrdemUsuario;

    private String exportacaoNfg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getTipoFinalidade() {
        return tipoFinalidade;
    }

    public void setTipoFinalidade(Integer tipoFinalidade) {
        this.tipoFinalidade = tipoFinalidade;
    }

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(Integer referencia) {
        this.referencia = referencia;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Integer getNumeroInscricao() {
        return numeroInscricao;
    }

    public void setNumeroInscricao(Integer numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public Date getDataInicioApuracao() {
        return dataInicioApuracao;
    }

    public void setDataInicioApuracao(Date dataInicioApuracao) {
        this.dataInicioApuracao = dataInicioApuracao;
    }

    public Date getDataFimApuracao() {
        return dataFimApuracao;
    }

    public void setDataFimApuracao(Date dataFimApuracao) {
        this.dataFimApuracao = dataFimApuracao;
    }

    public Integer getQuantidadeLinha() {
        return quantidadeLinha;
    }

    public void setQuantidadeLinha(Integer quantidadeLinha) {
        this.quantidadeLinha = quantidadeLinha;
    }

    public Date getDataCatalogacao() {
        return dataCatalogacao;
    }

    public void setDataCatalogacao(Date dataCatalogacao) {
        this.dataCatalogacao = dataCatalogacao;
    }

    public Date getDataInicioProcessamento() {
        return dataInicioProcessamento;
    }

    public void setDataInicioProcessamento(Date dataInicioProcessamento) {
        this.dataInicioProcessamento = dataInicioProcessamento;
    }

    public Date getDataFimProcessamento() {
        return dataFimProcessamento;
    }

    public void setDataFimProcessamento(Date dataFimProcessamento) {
        this.dataFimProcessamento = dataFimProcessamento;
    }

    public String getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(String statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public String getInfoContexto() {
        return infoContexto;
    }

    public void setInfoContexto(String infoContexto) {
        this.infoContexto = infoContexto;
    }

    public String getInfoDiretorio() {
        return infoDiretorio;
    }

    public void setInfoDiretorio(String infoDiretorio) {
        this.infoDiretorio = infoDiretorio;
    }

    public String getTipoModelo() {
        return tipoModelo;
    }

    public void setTipoModelo(String tipoModelo) {
        this.tipoModelo = tipoModelo;
    }

    public String getInfoMensagemLog() {
        return infoMensagemLog;
    }

    public void setInfoMensagemLog(String infoMensagemLog) {
        this.infoMensagemLog = infoMensagemLog;
    }

    public Date getDataEntregaSefaz() {
        return dataEntregaSefaz;
    }

    public void setDataEntregaSefaz(Date dataEntregaSefaz) {
        this.dataEntregaSefaz = dataEntregaSefaz;
    }

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public String getCpfContribuinte() {
        return cpfContribuinte;
    }

    public void setCpfContribuinte(String cpfContribuinte) {
        this.cpfContribuinte = cpfContribuinte;
    }

    public String getCnpjEstabelecimento() {
        return cnpjEstabelecimento;
    }

    public void setCnpjEstabelecimento(String cnpjEstabelecimento) {
        this.cnpjEstabelecimento = cnpjEstabelecimento;
    }

    public String getVersaoSoftware() {
        return versaoSoftware;
    }

    public void setVersaoSoftware(String versaoSoftware) {
        this.versaoSoftware = versaoSoftware;
    }

    public Date getDataGravacaoSoftware() {
        return dataGravacaoSoftware;
    }

    public void setDataGravacaoSoftware(Date dataGravacaoSoftware) {
        this.dataGravacaoSoftware = dataGravacaoSoftware;
    }

    public Integer getNumeroSequencial() {
        return numeroSequencial;
    }

    public void setNumeroSequencial(Integer numeroSequencial) {
        this.numeroSequencial = numeroSequencial;
    }

    public String getTipoComando() {
        return tipoComando;
    }

    public void setTipoComando(String tipoComando) {
        this.tipoComando = tipoComando;
    }

    public Integer getReducaoZInicio() {
        return reducaoZInicio;
    }

    public void setReducaoZInicio(Integer reducaoZInicio) {
        this.reducaoZInicio = reducaoZInicio;
    }

    public Integer getReducaoZFim() {
        return reducaoZFim;
    }

    public void setReducaoZFim(Integer reducaoZFim) {
        this.reducaoZFim = reducaoZFim;
    }

    public String getVersaoBiblioteca() {
        return versaoBiblioteca;
    }

    public void setVersaoBiblioteca(String versaoBiblioteca) {
        this.versaoBiblioteca = versaoBiblioteca;
    }

    public String getVersaoAtoCotepe() {
        return versaoAtoCotepe;
    }

    public void setVersaoAtoCotepe(String versaoAtoCotepe) {
        this.versaoAtoCotepe = versaoAtoCotepe;
    }

    public Integer getNumeroOrdemUsuario() {
        return numeroOrdemUsuario;
    }

    public void setNumeroOrdemUsuario(Integer numeroOrdemUsuario) {
        this.numeroOrdemUsuario = numeroOrdemUsuario;
    }

    public String getExportacaoNfg() {
        return exportacaoNfg;
    }

    public void setExportacaoNfg(String exportacaoNfg) {
        this.exportacaoNfg = exportacaoNfg;
    }
}
