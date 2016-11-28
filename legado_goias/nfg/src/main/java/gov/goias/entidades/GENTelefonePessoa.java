package gov.goias.entidades;

import gov.goias.persistencia.GENTelefonePessoaRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by bruno-cff on 07/12/2015.
 */

@Entity
@Table(name = "GEN_TELEFONE_PESSOA")
@Repository
public class GENTelefonePessoa extends GENTelefonePessoaRepository implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_TELEFONE")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "NUMR_DDD")
    private Integer ddd;

    @Column(name = "NUMR_TELEFONE")
    private Integer numero;

    @Column(name = "TIPO_TELEFONE")
    private Integer tipo;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA")
    private GENPessoaFisica genPessoaFisica;

    @Column(name = "INDI_HOMOLOG_CADASTRO")
    private String homologado;

    @Column(name = "NUMR_RAMAL")
    private Integer ramal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public GENPessoaFisica getGenPessoaFisica() {
        return genPessoaFisica;
    }

    public void setGenPessoaFisica(GENPessoaFisica genPessoaFisica) {
        this.genPessoaFisica = genPessoaFisica;
    }

    public String getHomologado() {
        return homologado;
    }

    public void setHomologado(String homologado) {
        this.homologado = homologado;
    }

    public Integer getRamal() {
        return ramal;
    }

    public void setRamal(Integer ramal) {
        this.ramal = ramal;
    }
}
