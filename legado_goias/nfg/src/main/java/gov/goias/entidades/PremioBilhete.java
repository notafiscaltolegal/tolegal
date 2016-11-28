package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import gov.goias.persistencia.PremioBilheteRepository;

/**
 * Created by bruno-cff on 06/05/2015.
 */
@Entity
@Table(name = "NFG_PREMIO_BILHETE")
@Repository
public class PremioBilhete extends PremioBilheteRepository implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PREMIO_BILHETE")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PREMIO_SORTEIO")
    @Fetch(value = FetchMode.JOIN)
    private PremioSorteio premioSorteio;

    @OneToOne
    @JoinColumn(name = "ID_BILHETE_PESSOA")
    @Fetch(value = FetchMode.JOIN)
    private BilhetePessoa bilhetePessoa;

    @Column(name = "NUMR_PREMIO_PROGRAMA_SORTEIO")
    private Long numeroPremio;

    @Column(name = "INFO_RESGATE")
    private String infoResgate;

    @Column(name = "DATA_SOLICT_RESGATE_PREMIO")
    private Date dataSolicitacaoResgate;

    @Column(name = "DATA_RESGATE_PREMIO")
    private Date dataResgate;

    @Column(name = "DATA_LIMITE_RESGATE")
    private Date dataLimiteResgate;

    @Column(name = "DATA_INCLUSAO_PREMIO")
    private Date dataInclusao;

    @Column(name = "VALR_TAXA_BANCARIA")
    private Double vlrTaxaBancaria;

    @Column(name = "VALR_TRIBUTO")
    private Double vlrTributo;

    @Column(name = "TIPO_RESGATE_PREMIO")
    private Character tipoResgate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PremioSorteio getPremioSorteio() {
        return premioSorteio;
    }

    public void setPremioSorteio(PremioSorteio premioSorteio) {
        this.premioSorteio = premioSorteio;
    }

    public BilhetePessoa getBilhetePessoa() {
        return bilhetePessoa;
    }

    public void setBilhetePessoa(BilhetePessoa bilhetePessoa) {
        this.bilhetePessoa = bilhetePessoa;
    }

    public Long getNumeroPremio() {
        return numeroPremio;
    }

    public void setNumeroPremio(Long numeroPremio) {
        this.numeroPremio = numeroPremio;
    }

    public Date getDataSolicitacaoResgate() {
        return dataSolicitacaoResgate;
    }

    public void setDataSolicitacaoResgate(Date dataSolicitacaoResgate) {
        this.dataSolicitacaoResgate = dataSolicitacaoResgate;
    }

    public Double getVlrTaxaBancaria() {
        return vlrTaxaBancaria;
    }

    public void setVlrTaxaBancaria(Double vlrTaxaBancaria) {
        this.vlrTaxaBancaria = vlrTaxaBancaria;
    }

    public Double getVlrTributo() {
        return vlrTributo;
    }

    public void setVlrTributo(Double vlrTributo) {
        this.vlrTributo = vlrTributo;
    }

    public Character getTipoResgate() {
        return tipoResgate;
    }

    public void setTipoResgate(Character tipoResgate) {
        this.tipoResgate = tipoResgate;
    }

    public String getInfoResgate() {
        return infoResgate;
    }

    public void setInfoResgate(String infoResgate) {
        this.infoResgate = infoResgate;
    }

    public Date getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(Date dataResgate) {
        this.dataResgate = dataResgate;
    }

    public Date getDataLimiteResgate() {
        return dataLimiteResgate;
    }

    public void setDataLimiteResgate(Date dataLimiteResgate) {
        this.dataLimiteResgate = dataLimiteResgate;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }
}

