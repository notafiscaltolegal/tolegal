package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Remisson-ss on 14/10/2014.
 */

public class ProcesmLoteDocumentoFiscal implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer numrLote;

    private Integer origemDocumentoFiscal;

    private StatusProcessamento statProcesmNfg;

    private String infoMotivoErroProcesm;

    private Date dataInicioProcessamento;

    private Date dataFimProcessamento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumrLote() {
        return numrLote;
    }

    public void setNumrLote(Integer numrLote) {
        this.numrLote = numrLote;
    }

    public Integer getOrigemDocumentoFiscal() {
        return origemDocumentoFiscal;
    }

    public void setOrigemDocumentoFiscal(Integer origemDocumentoFiscal) {
        this.origemDocumentoFiscal = origemDocumentoFiscal;
    }

    public StatusProcessamento getStatProcesmNfg() {
        return statProcesmNfg;
    }

    public void setStatProcesmNfg(StatusProcessamento statProcesmNfg) {
        this.statProcesmNfg = statProcesmNfg;
    }

    public String getInfoMotivoErroProcesm() {
        return infoMotivoErroProcesm;
    }

    public void setInfoMotivoErroProcesm(String infoMotivoErroProcesm) {
        this.infoMotivoErroProcesm = infoMotivoErroProcesm;
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
}
