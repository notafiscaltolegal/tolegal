package gov.goias.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NFE_IDENTIFICACAO")
@Inheritance(strategy = InheritanceType.JOINED)
public class NFEIdentificacao implements Serializable {

	private static final long serialVersionUID = 1049455724495796880L;

	@Id
	@Column(name = "ID_NFE")
	private Long id;

	@Column(name = "DESC_NATUREZA_OPERACAO")
	private String descricaoNaturezaOperacao;

	@Column(name = "NUMR_SERIE_NFE")
	private Integer numeroDeSerie;

	@Column(name = "DATA_EMISSAO_NFE")
	private Date dataEmissao;

	@Column(name = "NUMR_CNPJ_EMISSOR")
	private String cnpjEmissor;

	@Column(name = "NUMR_DOCUMENTO_FISCAL")
	private Integer numeroDocumentoFiscal;

	@Column(name = "DATA_MOVMT_MERCADORIA")
	private Date dataMovimentacaoMercadoria;

	@Column(name = "TIPO_DOCUMENTO_FISCAL")
	private String tipoDocumentoFiscal;

	@Column(name = "CODG_MUNICIPIO_GERADOR")
	private String codigoMunicipioGerador;

	@Column(name = "CODG_CHAVE_ACESSO_NFE")
	private String chaveDeAcesso;

	@Column(name = "TIPO_FORMATO_IMPRESSAO")
	private String tipoFormatoImpressao;

	@Column(name = "NUMR_PROTOCOLO")
	private String numeroProtocolo;

	@Column(name = "DATA_INICIO_PROCESM")
	private Date dataInicioProcessamento;

	@Column(name = "DATA_FIM_PROCESM")
	private Date dataFimProcessamento;

	@Column(name = "VALR_RESUMO_NFE")
	private String valorResumoNFE;

	@Column(name = "NUMR_PROTOCOLO_CANCEL")
	private String numeroProtocoloCancelamento;

	@Column(name = "NUMR_CPF_CNPJ_DEST")
	private String cpfCnpjDestinatario;

	@Column(name = "VALR_NOTA_FISCAL")
	private Double valorNotaFiscal;

	@Column(name = "VALR_ICMS")
	private Double valorICMS;

	@Column(name = "PERC_ALIQUOTA_ICMS")
	private Double percAliquotaICMS;

	@Column(name = "VALR_ICMS_SUBSTRIB")
	private Double valorICMSSubstrib;

	@Column(name = "PERC_ALIQUOTA_ICMS_SUBSTRIB")
	private Double percAliquotaICMSSubstrib;

    @Column(name = "NUMR_INSCRICAO_DEST")
    private String numeroInscricaoDestinatario;

	@Column(name = "TIPO_NFE")
	private String tipoNFE;

	@Column(name = "TIPO_FINALIDADE_NFE")
	private Integer tipoFinalidadeNFE;

	@Column(name = "NUMR_PLACA_TRANSP")
	private String placaTransportadora;

	@Column(name = "CODG_MUNICIPIO_DEST")
	private String codigoMunicipioDestino;

	@Column(name = "CODG_MUNICIPIO_EMISSOR")
	private String codigoMunicipioEmissor;

	@Column(name = "INDI_NOTA_EXPORTACAO")
	private Integer indicadorNotaExportacao;

	@Column(name = "STAT_VERIF_NFE")
	private String statVerifNFE;

	@ManyToOne
	@JoinColumn(name="NUMR_INSCRICAO", referencedColumnName="NUMR_INSCRICAO")
	private CCEContribuinte contribuinte;

	public NFEIdentificacao() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricaoNaturezaOperacao() {
		return descricaoNaturezaOperacao;
	}

	public void setDescricaoNaturezaOperacao(String descricaoNaturezaOperacao) {
		this.descricaoNaturezaOperacao = descricaoNaturezaOperacao;
	}

	public Integer getNumeroDeSerie() {
		return numeroDeSerie;
	}

	public void setNumeroDeSerie(Integer numeroDeSerie) {
		this.numeroDeSerie = numeroDeSerie;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	public Integer getNumeroDocumentoFiscal() {
		return numeroDocumentoFiscal;
	}

	public void setNumeroDocumentoFiscal(Integer numeroDocumentoFiscal) {
		this.numeroDocumentoFiscal = numeroDocumentoFiscal;
	}

	public Date getDataMovimentacaoMercadoria() {
		return dataMovimentacaoMercadoria;
	}

	public void setDataMovimentacaoMercadoria(Date dataMovimentacaoMercadoria) {
		this.dataMovimentacaoMercadoria = dataMovimentacaoMercadoria;
	}

	public String getTipoDocumentoFiscal() {
		return tipoDocumentoFiscal;
	}

	public void setTipoDocumentoFiscal(String tipoDocumentoFiscal) {
		this.tipoDocumentoFiscal = tipoDocumentoFiscal;
	}

	public String getCodigoMunicipioGerador() {
		return codigoMunicipioGerador;
	}

	public void setCodigoMunicipioGerador(String codigoMunicipioGerador) {
		this.codigoMunicipioGerador = codigoMunicipioGerador;
	}

	public String getChaveDeAcesso() {
		return chaveDeAcesso;
	}

	public void setChaveDeAcesso(String chaveDeAcesso) {
		this.chaveDeAcesso = chaveDeAcesso;
	}

	public String getTipoFormatoImpressao() {
		return tipoFormatoImpressao;
	}

	public void setTipoFormatoImpressao(String tipoFormatoImpressao) {
		this.tipoFormatoImpressao = tipoFormatoImpressao;
	}

	public String getNumeroProtocolo() {
		return numeroProtocolo;
	}

	public void setNumeroProtocolo(String numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
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

	public String getValorResumoNFE() {
		return valorResumoNFE;
	}

	public void setValorResumoNFE(String valorResumoNFE) {
		this.valorResumoNFE = valorResumoNFE;
	}

	public String getNumeroProtocoloCancelamento() {
		return numeroProtocoloCancelamento;
	}

	public void setNumeroProtocoloCancelamento(String numeroProtocoloCancelamento) {
		this.numeroProtocoloCancelamento = numeroProtocoloCancelamento;
	}

	public String getCpfCnpjDestinatario() {
		return cpfCnpjDestinatario;
	}

	public void setCpfCnpjDestinatario(String cpfCnpjDestinatario) {
		this.cpfCnpjDestinatario = cpfCnpjDestinatario;
	}

	public Double getValorNotaFiscal() {
		return valorNotaFiscal;
	}

	public void setValorNotaFiscal(Double valorNotaFiscal) {
		this.valorNotaFiscal = valorNotaFiscal;
	}

	public Double getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(Double valorICMS) {
		this.valorICMS = valorICMS;
	}

	public Double getPercAliquotaICMS() {
		return percAliquotaICMS;
	}

	public void setPercAliquotaICMS(Double percAliquotaICMS) {
		this.percAliquotaICMS = percAliquotaICMS;
	}

	public Double getValorICMSSubstrib() {
		return valorICMSSubstrib;
	}

	public void setValorICMSSubstrib(Double valorICMSSubstrib) {
		this.valorICMSSubstrib = valorICMSSubstrib;
	}

	public Double getPercAliquotaICMSSubstrib() {
		return percAliquotaICMSSubstrib;
	}

	public void setPercAliquotaICMSSubstrib(Double percAliquotaICMSSubstrib) {
		this.percAliquotaICMSSubstrib = percAliquotaICMSSubstrib;
	}

	public String getNumeroInscricaoDestinatario() {
		return numeroInscricaoDestinatario;
	}

	public void setNumeroInscricaoDestinatario(String numeroInscricaoDestinatario) {
		this.numeroInscricaoDestinatario = numeroInscricaoDestinatario;
	}

	public String getTipoNFE() {
		return tipoNFE;
	}

	public void setTipoNFE(String tipoNFE) {
		this.tipoNFE = tipoNFE;
	}

	public Integer getTipoFinalidadeNFE() {
		return tipoFinalidadeNFE;
	}

	public void setTipoFinalidadeNFE(Integer tipoFinalidadeNFE) {
		this.tipoFinalidadeNFE = tipoFinalidadeNFE;
	}

	public String getPlacaTransportadora() {
		return placaTransportadora;
	}

	public void setPlacaTransportadora(String placaTransportadora) {
		this.placaTransportadora = placaTransportadora;
	}

	public String getCodigoMunicipioDestino() {
		return codigoMunicipioDestino;
	}

	public void setCodigoMunicipioDestino(String codigoMunicipioDestino) {
		this.codigoMunicipioDestino = codigoMunicipioDestino;
	}

	public String getCodigoMunicipioEmissor() {
		return codigoMunicipioEmissor;
	}

	public void setCodigoMunicipioEmissor(String codigoMunicipioEmissor) {
		this.codigoMunicipioEmissor = codigoMunicipioEmissor;
	}

	public Integer getIndicadorNotaExportacao() {
		return indicadorNotaExportacao;
	}

	public void setIndicadorNotaExportacao(Integer indicadorNotaExportacao) {
		this.indicadorNotaExportacao = indicadorNotaExportacao;
	}

	public String getStatVerifNFE() {
		return statVerifNFE;
	}

	public void setStatVerifNFE(String statVerifNFE) {
		this.statVerifNFE = statVerifNFE;
	}

	public CCEContribuinte getContribuinte() {
		return contribuinte;
	}

	public void setContribuinte(CCEContribuinte contribuinte) {
		this.contribuinte = contribuinte;
	}

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof NFEIdentificacao)) return false;

        NFEIdentificacao outraNota = (NFEIdentificacao) obj;
        return getChaveDeAcesso().equals(outraNota.getChaveDeAcesso());
    }

    @Override
    public int hashCode() {
        return chaveDeAcesso != null ? chaveDeAcesso.hashCode() : 0;
    }
}
