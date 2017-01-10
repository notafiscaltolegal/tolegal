package gov.goias.entidades;

import gov.goias.persistencia.RegraPontuacaoDocumentoFiscalRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrique-rh on 08/07/14.
 */

@Entity
@Table(name = "NFG_REGRA_PONTUACAO_DOC_FISCAL")
@Repository
@Cacheable(true)
public class RegraPontuacaoDocumentoFiscal extends RegraPontuacaoDocumentoFiscalRepository implements Serializable {

    private static final long serialVersionUID = 3991462388185277420L;

    @Autowired
    @Transient
    SimpleDateFormat simpleDateFormat;

    @Id
    @Column(name = "ID_REGRA_PONTUACAO_DOC_FISCAL")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;



    @Column(name="VALR_FATOR_CONVERSAO")
    private Double valorFatorConversao;

    @Column(name="NOME_REGRA_PONTUACAO_DOC_FISC")
    private String nome;

    @Column(name="NUMR_MAXIMO_PONTO_DOCUMENTO")
    private Integer numrMaximoPontoDocumento;

    @Column(name="NUMR_MAXIMO_PONTO_REFERENCIA")
    private Integer numrMaximoPontoRef;

    @Column(name="NUMR_MAXIMO_DOC_ESTAB_REF")
    private Integer numrMaximoEstabRef;

    @Column(name="NUMR_MAXIMO_DOCUMENTO_REF")
    private Integer numrMaximoDocRef;

    @Column(name="DATA_INICIO_REGRA")
    private Date dataInicioRegra;

    @Column(name="DATA_FIM_REGRA")
    private Date dataFimRegra;

    @Transient
    private String dataFimRegraStr;
    @Transient
    private String dataInicioRegraStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Double getValorFatorConversao() {
        return valorFatorConversao;
    }

    public void setValorFatorConversao(Double valorFatorConversao) {
        this.valorFatorConversao = valorFatorConversao;
    }

      public Date getDataInicioRegra() {
        return dataInicioRegra;
    }

    public void setDataInicioRegra(Date dataInicioRegra) {
        this.dataInicioRegra = dataInicioRegra;
    }

    public Date getDataFimRegra() {
        return dataFimRegra;
    }

    public void setDataFimRegra(Date dataFimRegra) {
        this.dataFimRegra = dataFimRegra;
    }

    public Integer getNumrMaximoPontoDocumento() {
        return numrMaximoPontoDocumento;
    }

    public void setNumrMaximoPontoDocumento(Integer numrMaximoPontoDocumento) {
        this.numrMaximoPontoDocumento = numrMaximoPontoDocumento;
    }

    public Integer getNumrMaximoPontoRef() {
        return numrMaximoPontoRef;
    }

    public void setNumrMaximoPontoRef(Integer numrMaximoPontoRef) {
        this.numrMaximoPontoRef = numrMaximoPontoRef;
    }

    public Integer getNumrMaximoEstabRef() {
        return numrMaximoEstabRef;
    }

    public void setNumrMaximoEstabRef(Integer numrMaximoEstabRef) {
        this.numrMaximoEstabRef = numrMaximoEstabRef;
    }

    public Integer getNumrMaximoDocRef() {
        return numrMaximoDocRef;
    }

    public void setNumrMaximoDocRef(Integer numrMaximoDocRef) {
        this.numrMaximoDocRef = numrMaximoDocRef;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataFimRegraStr() {
        Date dataFim = getDataFimRegra();
        return dataFim!=null?simpleDateFormat.format(dataFim):"Não informado";
    }

    public String getDataInicioRegraStr() {
        Date dataIni= getDataInicioRegra();
        return dataIni!=null?simpleDateFormat.format(dataIni):"Não informado";
    }
}
