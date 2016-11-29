package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Remisson-ss on 14/10/2014.
 */


@Entity
@Table(name = "NFG_DOCUMENTO_FISCAL_PARTCT")
@Repository
public class DocumentoFiscalParticipante  extends GenericRepository<Integer, DocumentoFiscalParticipante> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_DOCUMENTO_FISCAL_PARTCT")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "DATA_IMPORTACAO")
    private Date dataImportacao;

    @Column(name = "ID_NFE")
    private Integer idNfe;

    @Column(name = "ID_NOTA_FISCAL_EMITIDA_ECF")
    private Integer idEFD;

    @Column(name = "ID_CUPOM_FISCAL_ECF")
    private Integer idMFD;

    @Column(name = "ID_DOCUMENTO_FISCAL_DIGITADO")
    private Integer idDocFiscalDigitado;

    @Column(name = "NUMR_CPF_ADQUIRENTE")
    private String numeroCpf;

    @Column(name = "NUMR_DOCUMENTO_FISCAL")
    private Integer numeroDocumentoFiscal;

    @Column(name = "DATA_EMISSAO_DOCUMENTO_FISCAL")
    private Date dataEmissaoDocumento;

    @Column(name = "VALR_TOTAL_DOCUMENTO_FISCAL")
    private Double valorDocumento;

    @Column(name = "NUMR_CNPJ_ESTAB")
    private String numeroCnpjEmissor;

    @Column(name = "STAT_PROCESM_DOCUMENTO_FISCAL")
    private Integer statusProcessamento;

    @ManyToOne
    @JoinColumn(name = "ID_PROCESM_LOTE_DOC_FISCAL")
    ProcesmLoteDocumentoFiscal procesmLoteDocumentoFiscal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(Date dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public ProcesmLoteDocumentoFiscal getProcesmLoteDocumentoFiscal() {
        return procesmLoteDocumentoFiscal;
    }

    public void setProcesmLoteDocumentoFiscal(ProcesmLoteDocumentoFiscal procesmLoteDocumentoFiscal) {
        this.procesmLoteDocumentoFiscal = procesmLoteDocumentoFiscal;
    }

    public Integer getIdNfe() {
        return idNfe;
    }

    public void setIdNfe(Integer idNfe) {
        this.idNfe = idNfe;
    }

    public Integer getIdEFD() {
        return idEFD;
    }

    public void setIdEFD(Integer idEFD) {
        this.idEFD = idEFD;
    }

    public Integer getIdMFD() {
        return idMFD;
    }

    public void setIdMFD(Integer idMFD) {
        this.idMFD = idMFD;
    }

    public Integer getIdDocFiscalDigitado() {
        return idDocFiscalDigitado;
    }

    public void setIdDocFiscalDigitado(Integer idDocFiscalDigitado) {
        this.idDocFiscalDigitado = idDocFiscalDigitado;
    }

    public String getNumeroCpf() {
        return numeroCpf;
    }

    public void setNumeroCpf(String numeroCpf) {
        this.numeroCpf = numeroCpf;
    }

    public Integer getNumeroDocumentoFiscal() {
        return numeroDocumentoFiscal;
    }

    public void setNumeroDocumentoFiscal(Integer numeroDocumentoFiscal) {
        this.numeroDocumentoFiscal = numeroDocumentoFiscal;
    }

    public Date getDataEmissaoDocumento() {
        return dataEmissaoDocumento;
    }

    public void setDataEmissaoDocumento(Date dataEmissaoDocumento) {
        this.dataEmissaoDocumento = dataEmissaoDocumento;
    }

    public Double getValorDocumento() {
        return valorDocumento;
    }

    public void setValorDocumento(Double valorDocumento) {
        this.valorDocumento = valorDocumento;
    }

    public String getNumeroCnpjEmissor() {
        return numeroCnpjEmissor;
    }

    public void setNumeroCnpjEmissor(String numeroCnpjEmissor) {
        this.numeroCnpjEmissor = numeroCnpjEmissor;
    }

    public Integer getStatusProcessamento() {
        return statusProcessamento;
    }

    public void setStatusProcessamento(Integer statusProcessamento) {
        this.statusProcessamento = statusProcessamento;
    }

    public Integer getIdReferenciaDocumento() {
        if (this.idMFD != null) {
            return this.idMFD;
        }
        if (this.idEFD != null) {
            return this.idEFD;
        }
        if (this.idNfe != null) {
            return this.idNfe;
        }
        if (this.idDocFiscalDigitado != null) {
            return this.idDocFiscalDigitado;
        }
        return null;
    }

    public Integer getTipoDocumentoFiscal(){
        if (this.idMFD != null) {
            return 3;
        }
        if (this.idEFD != null) {
            return 3;
        }
        if (this.idNfe != null) {
            return 2;
        }
        if (this.idDocFiscalDigitado != null) {
            return 1;
        }
        return null;
    }

}