package gov.goias.entidades;

import gov.goias.persistencia.GENEmailPessoaRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 03/12/2015.
 */

@Entity
@Table(name = "GEN_EMAIL_PESSOA")
@Repository
public class GENEmailPessoa extends GENEmailPessoaRepository implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_EMAIL_PESSOA")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "DESC_EMAIL_PESSOA")
    private String descricao;

    @Column(name = "TIPO_EMAIL_PESSOA")
    private Integer tipo;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA")
    private GENPessoaFisica genPessoaFisica;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
}