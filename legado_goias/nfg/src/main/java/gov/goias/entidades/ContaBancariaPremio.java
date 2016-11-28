package gov.goias.entidades;

import gov.goias.persistencia.ContaBancariaPremioRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 16/06/2015.
 */

@Entity
@Table(name = "NFG_CONTA_BANCARIA_PREMIO")
@Repository
public class ContaBancariaPremio extends ContaBancariaPremioRepository implements Serializable{

    @Id
    @OneToOne
    @JoinColumn(name = "ID_PREMIO_BILHETE")
    private PremioBilhete premioBilhete;

    @Column(name = "TIPO_CONTA_BANCARIA")
    private Integer tipo;

    @Column(name = "NUMR_CONTA_BANCARIA")
    private Integer numero;

    @Column(name = "NUMR_DIGITO_CONTA_BANCARIA")
    private String digito;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "CODG_AGENCIA", referencedColumnName = "CODG_AGENCIA"),
            @JoinColumn(name = "CODG_BANCO", referencedColumnName = "CODG_BANCO")
    })
    private GENAgenciaBancaria agenciaBancaria;

//    @ManyToOne
//    @JoinColumn(name = "CODG_BANCO")
//    private GENBanco banco;

    public PremioBilhete getPremioBilhete() {
        return premioBilhete;
    }

    public void setPremioBilhete(PremioBilhete premioBilhete) {
        this.premioBilhete = premioBilhete;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDigito() {
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    public GENAgenciaBancaria getAgenciaBancaria() {
        return agenciaBancaria;
    }

    public void setAgenciaBancaria(GENAgenciaBancaria agenciaBancaria) {
        this.agenciaBancaria = agenciaBancaria;
    }

//    public GENBanco getBanco() {
//        return banco;
//    }
//
//    public void setBanco(GENBanco banco) {
//        this.banco = banco;
//    }
}
