package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Remisson-ss on 14/10/2014.
 */

@Entity
@Table(name = "NFG_PONTUACAO_EXTRA_PESSOA")
@Repository
public class PontuacaoExtraPessoa extends GenericRepository<Integer, PontuacaoExtraPessoa> implements Serializable
{
    @Id
    @OneToOne
    @JoinColumn(name = "ID_PONTUACAO_PESSOA")
    PontuacaoPessoa pontuacaoPessoa;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PONTUACAO_EXTRA")
    private RegraPontuacaoExtra regraPontuacaoExtra;

    public RegraPontuacaoExtra getRegraPontuacaoExtra() {
        return regraPontuacaoExtra;
    }

    public void setRegraPontuacaoExtra(RegraPontuacaoExtra regraPontuacaoExtra) {
        this.regraPontuacaoExtra = regraPontuacaoExtra;
    }

    public PontuacaoPessoa getPontuacaoPessoa() {
        return pontuacaoPessoa;
    }

    public void setPontuacaoPessoa(PontuacaoPessoa pontuacaoPessoa) {
        this.pontuacaoPessoa = pontuacaoPessoa;
    }
}
