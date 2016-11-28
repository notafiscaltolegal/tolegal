package gov.goias.entidades;

import gov.goias.persistencia.GENAgenciaBancariaRepository;
import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 12/06/2015.
 */

@Entity
@Table(name = "GEN_AGENCIA_BANCARIA")
@Repository
public class GENAgenciaBancaria extends GENAgenciaBancariaRepository implements Serializable{

    @Id
    @Column(name = "CODG_AGENCIA")
    private String codigoAgencia;

    @Id
    @Column(name = "CODG_BANCO")
    private String codigoBanco;

    @Column(name = "NOME_AGENCIA")
    private String nomeAgencia;

    @ManyToOne
    @JoinColumn(name = "CODG_BANCO")
    private GENBanco banco;

    @Column(name = "INDI_SITUACAO")
    private String situacao;

    @Column(name = "NOME_CONTATO")
    private String nomeContato;

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public String getNomeAgencia() {
        return nomeAgencia;
    }

    public void setNomeAgencia(String nomeAgencia) {
        this.nomeAgencia = nomeAgencia;
    }

    public GENBanco getBanco() {
        return banco;
    }

    public void setBanco(GENBanco banco) {
        this.banco = banco;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }
}

