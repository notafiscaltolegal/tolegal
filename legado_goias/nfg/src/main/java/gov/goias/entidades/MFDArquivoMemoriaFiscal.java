package gov.goias.entidades;

import gov.goias.persistencia.MFDArquivoMemoriaFiscalRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by bruno-cff on 01/07/2015.
 */
@Entity
@Table(name = "MFD_ARQUIVO_MEMORIA_FISCAL_ECF")
@Repository
public class MFDArquivoMemoriaFiscal extends MFDArquivoMemoriaFiscalRepository{

    @Id
    @Column(name = "ID_ARQUIVO_MEMORIA_FISCAL_ECF")
    private Integer id;

    @Column(name = "NOME_ARQUIVO")
    private String nome;

    @Column(name = "CODG_VERSAO_LEIAUTE")
    private Integer codigo;

    @Column(name = "TIPO_FINALIDADE_ARQUIVO")
    private Integer tipoFinalidade;

    @Column(name = "REFE_ARQUIVO")
    private Integer referencia;

    @Column(name = "DATA_ENTREGA_ARQUIVO")
    private Date dataEntrega;

    @Column(name = "NUMR_INSCRICAO")
    private Integer numeroInscricao;

    @Column(name = "DATA_INICIO_APURACAO")
    private Date dataInicioApuracao;

    @Column(name = "DATA_FIM_APURACAO")
    private Date dataFimApuracao;

    @Column(name = "QTDE_LINHA_ARQUIVO")
    private Integer quantidadeLinha;

    @Column(name = "DATA_CATALOG_ARQUIVO")
    private Date dataCatalogacao;

    @Column(name = "DATA_INICIO_PROCESM")
    private Date dataInicioProcessamento;

    @Column(name = "DATA_FIM_PROCESM")
    private Date dataFimProcessamento;

    @Column(name = "STAT_PROCESM_ARQUIVO_ECF")
    private String statusProcessamento;

    @Column(name = "INFO_CONTEXTO")
    private String infoContexto;

    @Column(name = "INFO_DIRETORIO")
    private String infoDiretorio;

    @Column(name = "TIPO_MODELO_ARQ_ECF")
    private String tipoModelo;

    @Column(name = "INFO_MENSAGEM_LOG_ERRO")
    private String infoMensagemLog;

    @Column(name = "DATA_ENTREGA_ARQUIVO_SEFAZ")
    private Date dataEntregaSefaz;

    @Column(name = "INFO_HASH_ARQUIVO")
    private String infoHash;

    @Column(name = "NUMR_CPF_CONTRIB")
    private String cpfContribuinte;

    @Column(name = "NUMR_CNPJ_ESTAB_USUARIO")
    private String cnpjEstabelecimento;

    @Column(name = "INFO_VERSAO_SOFTWARE_BASICO")
    private String versaoSoftware;

    @Column(name = "DATA_GRAVACAO_SOFTWARE_BASICO")
    private Date dataGravacaoSoftware;

    @Column(name = "NUMR_SEQUENCIAL_ECF")
    private Integer numeroSequencial;

    @Column(name = "TIPO_COMANDO_GERACAO_ARQUIVO")
    private String tipoComando;

    @Column(name = "NUMR_CONTADOR_REDUCAO_Z_INICIO")
    private Integer reducaoZInicio;

    @Column(name = "NUMR_CONTADOR_REDUCAO_Z_FIM")
    private Integer reducaoZFim;

    @Column(name = "INFO_VERSAO_BIBLIOTECA")
    private String versaoBiblioteca;

    @Column(name = "INFO_VERSAO_ATO_COTEPE")
    private String versaoAtoCotepe;

    @Column(name = "NUMR_ORDEM_USUARIO")
    private Integer numeroOrdemUsuario;

    @Column(name = "INDI_EXPORTACAO_NFG")
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
