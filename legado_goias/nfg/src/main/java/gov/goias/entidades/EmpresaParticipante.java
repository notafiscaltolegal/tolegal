package gov.goias.entidades;

import gov.goias.cache.EmpresaParticipanteCache;
import gov.goias.persistencia.EmpresaParticipanteRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by diogo-rs on 7/16/2014.
 */
@Entity
@Table(name = "NFG_EMPRESA_PARTICIPANTE_NFG")
@Repository
@Cacheable(true)
public class EmpresaParticipante extends EmpresaParticipanteCache implements Serializable{

    private static final long serialVersionUID = 9196489902560654798L;

    public static final Character INDI_CONTADOR = 'C';
    public static final Character INDI_EMPRESA = 'E';

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    @Column(name = "ID_EMPRESA_PARTCT_NFG")
    private Integer id;

    @Lob
    @Column(name = "INFO_TERMO_ACORDO_ADESAO")
    private byte[] termoDeAcordo;

    @Column(name = "DATA_CREDENC")
    private Date dataCredenciamento;

    @Column(name = "DATA_DESCREDENC")
    private Date dataDescredenciamento;

    @Column(name = "INFO_MOTIVO_DESCREDENC", length = 4000)
    private String motivoDescredenciamento;

    @Column(name = "DATA_EFETIVA_PARTCP")
    private Date dataEfetivaParticipacao;

    @Column(name = "INDI_RESP_ADESAO")
    private Character indiResponsavel;

    @Column(name = "ID_PESSOA_ADESAO")
    private Integer idPessoaAdesao;

    @OneToOne
    @JoinColumn(name = "NUMR_INSCRICAO")
    private CCEContribuinte contribuinte;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getTermoDeAcordo() {
        return termoDeAcordo;
    }

    public void setTermoDeAcordo(byte[] termoDeAcordo) {
        this.termoDeAcordo = Arrays.copyOf(termoDeAcordo, termoDeAcordo.length);
    }

    public Date getDataCredenciamento() {
        return dataCredenciamento;
    }

    public void setDataCredenciamento(Date dataCredenciamento) {
        this.dataCredenciamento = dataCredenciamento;
    }

    public Date getDataDescredenciamento() {
        return dataDescredenciamento;
    }

    public void setDataDescredenciamento(Date dataDescredenciamento) {
        this.dataDescredenciamento = dataDescredenciamento;
    }

    public String getMotivoDescredenciamento() {
        return motivoDescredenciamento;
    }

    public void setMotivoDescredenciamento(String motivoDescredenciamento) {
        this.motivoDescredenciamento = motivoDescredenciamento;
    }

    public CCEContribuinte getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(CCEContribuinte contribuinte) {
        this.contribuinte = contribuinte;
    }

    public Date getDataEfetivaParticipacao() {
        return dataEfetivaParticipacao;
    }

    public void setDataEfetivaParticipacao(Date dataEfetivaParticipacao) {
        this.dataEfetivaParticipacao = dataEfetivaParticipacao;
    }

    public Character getIndiResponsavel() {
        return indiResponsavel;
    }

    public void setIndiResponsavel(Character indiResponsavel) {
        this.indiResponsavel = indiResponsavel;
    }

    public Integer getIdPessoaAdesao() {
        return idPessoaAdesao;
    }

    public void setIdPessoaAdesao(Integer idPessoaAdesao) {
        this.idPessoaAdesao = idPessoaAdesao;
    }
}
