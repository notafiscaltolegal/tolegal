package gov.to.goias;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by allan-ca on 9/17/2014.
 */
public class DocumentoFiscalDigitadoToLegal implements Serializable{

    private static final long serialVersionUID = 9196489902560654798L;

    public static final Integer TIPO_NOTA_MODELO_1 = 1;
    public static final Integer TIPO_NOTA_MODELO_2 = 2;
    public static final Integer TIPO_NOTA_ECF_ANTIGO = 3;

    public static final Integer SERIE_NOTA_D = 1;
    public static final Integer SERIE_NOTA_D_UNICA = 2;
    public static final Integer SERIE_NOTA_UNICA = 3;

    private Integer id;

    private Integer numeroDocumentoFiscal;

    private Integer serieNotaFiscal;

    private Integer tipoDocumentoFiscal;

    private String cpf;

    private Integer numeroLote;

    private Double valorTotal;

    private Date dataInclucaoDoctoFiscal;

    private Date dataEmissao;

    private Date dataCancelDocumentoFiscal;

    private String subSerieNotaFiscal;

    //private CCEContribuinte contribuinte;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroDocumentoFiscal() {
        return numeroDocumentoFiscal;
    }

    public void setNumeroDocumentoFiscal(Integer numeroDocumentoFiscal) {
        this.numeroDocumentoFiscal = numeroDocumentoFiscal;
    }

    public Integer getSerieNotaFiscal() {
        return serieNotaFiscal;
    }

    public void setSerieNotaFiscal(Integer serieNotaFiscal) {
        this.serieNotaFiscal = serieNotaFiscal;
    }

    public Integer getTipoDocumentoFiscal() {
        return tipoDocumentoFiscal;
    }

    public void setTipoDocumentoFiscal(Integer tipoDocumentoFiscal) {
        this.tipoDocumentoFiscal = tipoDocumentoFiscal;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataInclucaoDoctoFiscal() {
        return dataInclucaoDoctoFiscal;
    }

    public void setDataInclucaoDoctoFiscal(Date dataInclucaoDoctoFiscal) {
        this.dataInclucaoDoctoFiscal = dataInclucaoDoctoFiscal;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getSubSerieNotaFiscal() {
        return subSerieNotaFiscal;
    }

    public void setSubSerieNotaFiscal(String subSerieNotaFiscal) {
        this.subSerieNotaFiscal = subSerieNotaFiscal;
    }

    /*public CCEContribuinte getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(CCEContribuinte contribuinte) {
        this.contribuinte = contribuinte;
    }*/

    public Date getDataCancelDocumentoFiscal() {
        return dataCancelDocumentoFiscal;
    }

    public void setDataCancelDocumentoFiscal(Date dataCancelDocumentoFiscal) {
        this.dataCancelDocumentoFiscal = dataCancelDocumentoFiscal;
    }

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }
}
