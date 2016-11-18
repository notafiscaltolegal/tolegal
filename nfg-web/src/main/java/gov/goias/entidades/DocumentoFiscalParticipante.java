package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Remisson-ss on 14/10/2014.
 */


public class DocumentoFiscalParticipante implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Date dataImportacao;

    private Integer idNfe;

    private Integer idEFD;

    private Integer idMFD;

    private Integer idDocFiscalDigitado;

    private String numeroCpf;

    private Integer numeroDocumentoFiscal;

    private Date dataEmissaoDocumento;

    private Double valorDocumento;

    private String numeroCnpjEmissor;

    private Integer statusProcessamento;

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
