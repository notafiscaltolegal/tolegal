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
@Table(name = "NFG_REGRA_PONTUACAO_EXTRA")
@Repository
@Cacheable(true)
public class RegraPontuacaoExtra  extends GenericRepository<Integer, RegraPontuacaoExtra> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PONTUACAO_EXTRA")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name="DESC_PONTUACAO_EXTRA")
    private String descPontuacaoExtra;

    @Column(name="QTDE_PONTO")
    private Integer qtdePontos;

    @Column(name="DATA_INICIO_REGRA")
    private Date dataInicioRegra;

    @Column(name="DATA_FIM_REGRA")
    private Date dataFimRegra;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescPontuacaoExtra() {
        return descPontuacaoExtra;
    }

    public void setDescPontuacaoExtra(String descPontuacaoExtra) {
        this.descPontuacaoExtra = descPontuacaoExtra;
    }

    public Date getDataInicioRegra() {
        return dataInicioRegra;
    }

    public void setDataInicioRegra(Date dataInicioRegra) {
        this.dataInicioRegra = dataInicioRegra;
    }

    public Integer getQtdePontos() {
        return qtdePontos;
    }

    public void setQtdePontos(Integer qtdePontos) {
        this.qtdePontos = qtdePontos;
    }

    public Date getDataFimRegra() {
        return dataFimRegra;
    }

    public void setDataFimRegra(Date dataFimRegra) {
        this.dataFimRegra = dataFimRegra;
    }
}
