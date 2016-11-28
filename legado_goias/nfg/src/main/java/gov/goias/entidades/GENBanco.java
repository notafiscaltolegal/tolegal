package gov.goias.entidades;

import gov.goias.persistencia.GENBancoRepository;
import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bruno-cff on 12/06/2015.
 */
@Entity
@Table(name = "GEN_BANCO")
@Repository
public class GENBanco extends GENBancoRepository implements Serializable{

    @Id
    @Column(name = "CODG_BANCO")
    private Integer codigo;

    @Column(name = "SIGL_BANCO")
    private String sigla;

    @Column(name = "NOME_BANCO")
    private String nome;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
