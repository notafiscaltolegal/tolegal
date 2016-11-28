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
@Table(name = "NFG_PONTUACAO_PESSOA")
@Repository
public class PontuacaoPessoa  extends GenericRepository<Integer, PontuacaoPessoa> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PONTUACAO_PESSOA")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;


    @Column(name = "DATA_PONTUACAO")
    private Date dataPontuacao;

    @Column(name = "INDI_ORIGEM_PONTUACAO_NFG")
    private Integer indiOrigem;

    @Column(name = "QTDE_PONTO")
    private Integer qtdePontos;

    @Column(name = "STAT_PONTUACAO_NFG")
    private Integer statusPontuacao;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA_PARTCT")
    PessoaParticipante pessoaParticipante;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getDataPontuacao() {
        return dataPontuacao;
    }

    public void setDataPontuacao(Date dataPontuacao) {
        this.dataPontuacao = dataPontuacao;
    }

    public PessoaParticipante getPessoaParticipante() {
        return pessoaParticipante;
    }

    public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
        this.pessoaParticipante = pessoaParticipante;
    }

    public Integer getIndiOrigem() {
        return indiOrigem;
    }

    public void setIndiOrigem(Integer indiOrigem) {
        this.indiOrigem = indiOrigem;
    }

    public Integer getQtdePontos() {
        return qtdePontos;
    }

    public void setQtdePontos(Integer qtdePontos) {
        this.qtdePontos = qtdePontos;
    }

    public Integer getStatusPontuacao() {
        return statusPontuacao;
    }

    public void setStatusPontuacao(Integer statusPontuacao) {
        this.statusPontuacao = statusPontuacao;
    }

}
