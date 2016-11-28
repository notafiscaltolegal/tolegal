package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 28/05/2015.
 */
@Entity
@Table(name = "NFG_SORTEIO_PONTUACAO")
@Repository
public class SorteioPontuacao extends GenericRepository<Integer, SorteioPontuacao> implements Serializable{

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PONTUACAO_PESSOA")
    private PontuacaoPessoa pontuacaoPessoa;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_REGRA_SORTEIO")
    private RegraSorteio regraSorteio;

    public PontuacaoPessoa getPontuacaoPessoa() {
        return pontuacaoPessoa;
    }

    public void setPontuacaoPessoa(PontuacaoPessoa pontuacaoPessoa) {
        this.pontuacaoPessoa = pontuacaoPessoa;
    }

    public RegraSorteio getRegraSorteio() {
        return regraSorteio;
    }

    public void setRegraSorteio(RegraSorteio regraSorteio) {
        this.regraSorteio = regraSorteio;
    }
}
