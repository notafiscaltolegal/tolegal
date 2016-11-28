package gov.goias.entidades;

import gov.goias.persistencia.BilhetePessoaRepository;
import gov.goias.persistencia.historico.HistoricoNFG;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 06/05/2015.
 */
@Entity
@Table(name = "NFG_BILHETE_PESSOA")
@Repository
public class BilhetePessoa extends BilhetePessoaRepository implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_BILHETE_PESSOA")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "INDI_NUMERO_BILHETE_DEFINITIVO")
    private Character indiNumeroBilheteDefinitivo;

    @Column(name = "NUMR_SEQUENCIAL_BILHETE")
    private Integer numeroSequencial;

    @ManyToOne
    @JoinColumn(name = "ID_REGRA_SORTEIO")
    private RegraSorteio regraSorteio;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA_PARTCT")
    private PessoaParticipante pessoaParticipante;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroSequencial() {
        return numeroSequencial;
    }

    public void setNumeroSequencial(Integer numeroSequencial) {
        this.numeroSequencial = numeroSequencial;
    }

    public Character getIndiNumeroBilheteDefinitivo() {
        return indiNumeroBilheteDefinitivo;
    }

    public void setIndiNumeroBilheteDefinitivo(Character indiNumeroBilheteDefinitivo) {
        this.indiNumeroBilheteDefinitivo = indiNumeroBilheteDefinitivo;
    }

    public RegraSorteio getRegraSorteio() {
        return regraSorteio;
    }

    public void setRegraSorteio(RegraSorteio regraSorteio) {
        this.regraSorteio = regraSorteio;
    }

    public PessoaParticipante getPessoaParticipante() {
        return pessoaParticipante;
    }

    public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
        this.pessoaParticipante = pessoaParticipante;
    }

}
