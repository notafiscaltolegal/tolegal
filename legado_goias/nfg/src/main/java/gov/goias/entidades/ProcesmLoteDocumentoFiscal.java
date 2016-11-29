package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Remisson-ss on 14/10/2014.
 */

@Entity
@Table(name = "NFG_PROCESM_LOTE_DOC_FISCAL")
@Repository
public class ProcesmLoteDocumentoFiscal  extends GenericRepository<Integer, ProcesmLoteDocumentoFiscal> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PROCESM_LOTE_DOC_FISCAL")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name="NUMR_LOTE")
    private Integer numrLote;

    @Column(name="TIPO_ORIGEM_DOCUMENTO_FISCAL")
    private Integer origemDocumentoFiscal;

    @Column(name="STAT_PROCESM_LOTE_DOC_FISCAL")
    private Character statProcesmNfg;

    @Column(name="INFO_MOTIVO_ERRO_PROCESM")
    private String infoMotivoErroProcesm;

    @Column(name = "DATA_INICIO_PROCESM_LOTE")
    private Date dataInicioProcessamento;

    @Column(name = "DATA_FIM_PROCESM_LOTE")
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

    public Character getStatProcesmNfg() {
        return statProcesmNfg;
    }

    public void setStatProcesmNfg(Character statProcesmNfg) {
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