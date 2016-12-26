package gov.goias.entidades;

import java.io.Serializable;

/**
 * Created by bruno-cff on 06/05/2015.
 */
public class BilhetePessoa implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Character indiNumeroBilheteDefinitivo;

    private Integer numeroSequencial;

    private RegraSorteio regraSorteio;

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
