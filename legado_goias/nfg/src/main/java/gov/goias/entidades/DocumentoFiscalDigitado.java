package gov.goias.entidades;

import gov.goias.persistencia.DocumentoFiscalDigitadoRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by allan-ca on 9/17/2014.
 */
@Entity
@Table(name = "NFG_DOCUMENTO_FISCAL_DIGITADO")
@Repository
public class DocumentoFiscalDigitado extends DocumentoFiscalDigitadoRepository implements Serializable{

    private static final long serialVersionUID = 9196489902560654798L;

    public static final Integer TIPO_NOTA_MODELO_1 = 1;
    public static final Integer TIPO_NOTA_MODELO_2 = 2;
    public static final Integer TIPO_NOTA_ECF_ANTIGO = 3;

    public static final Integer SERIE_NOTA_D = 1;
    public static final Integer SERIE_NOTA_D_UNICA = 2;
    public static final Integer SERIE_NOTA_UNICA = 3;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    @Column(name = "ID_DOCUMENTO_FISCAL_DIGITADO")
    private Integer id;

    @Column(name = "NUMR_DOCUMENTO_FISCAL")
    private Integer numeroDocumentoFiscal;

    @Column(name = "INDI_SERIE_NOTA_DIGITADA")
    private Integer serieNotaFiscal;

    @Column(name = "TIPO_DOCUMENTO_FISCAL_DIGITADO")
    private Integer tipoDocumentoFiscal;

    @Column(name = "NUMR_CPF_DEST")
    private String cpf;

    @Column(name = "NUMR_LOTE")
    private Integer numeroLote;

    @Column(name = "VALR_TOTAL_DOCUMENTO_FISCAL")
    private Double valorTotal;

    @Column(name = "DATA_INCLUSAO_DOCUMENTO_FISCAL")
    private Date dataInclucaoDoctoFiscal;

    @Column(name = "DATA_EMISSAO_DOCUMENTO_FISCAL")
    private Date dataEmissao;

    @Column(name = "DATA_CANCEL_DOCUMENTO_FISCAL")
    private Date dataCancelDocumentoFiscal;

    @Column(name = "NUMR_SUBSERIE_NOTA_DIGITADA", length = 2)
    private String subSerieNotaFiscal;

    @OneToOne
    @JoinColumn(name = "NUMR_INSCRICAO")
    private CCEContribuinte contribuinte;

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

    public CCEContribuinte getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(CCEContribuinte contribuinte) {
        this.contribuinte = contribuinte;
    }

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
