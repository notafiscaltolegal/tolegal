package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by thiago-mb on 23/07/2014.
 */
@Entity
@Table(name = "CCE_SECAO_CNAE_FISCAL")
@Repository
@Cacheable(true)
public class CCESecaoCnae extends GenericRepository<Integer, CCESecaoCnae> implements Serializable {

    private static final long serialVersionUID = -5190315070518228162L;
    @Id
    @Column(name = "ID_SECAO_CNAEF", nullable = false)
    private Integer idSecaoCnae;

    @Column(name = "CODG_SECAO_CNAEF", nullable = false)
    private String codSecaoCnae;

    @Column(name = "STAT_SECAO_CNAEF", nullable = false)
    private Character statSecaoCnae;

    @Column(name = "DESC_SECAO_CNAEF", nullable = false)
    private String descSecaoCnae;

    public Integer getIdSecaoCnae() {
        return idSecaoCnae;
    }

    public void setIdSecaoCnae(Integer idSecaoCnae) {
        this.idSecaoCnae = idSecaoCnae;
    }

    public String getCodSecaoCnae() {
        return codSecaoCnae;
    }

    public void setCodSecaoCnae(String codSecaoCnae) {
        this.codSecaoCnae = codSecaoCnae;
    }

    public Character getStatSecaoCnae() {
        return statSecaoCnae;
    }

    public void setStatSecaoCnae(Character statSecaoCnae) {
        this.statSecaoCnae = statSecaoCnae;
    }

    public String getDescSecaoCnae() {
        return descSecaoCnae;
    }

    public void setDescSecaoCnae(String descSecaoCnae) {
        this.descSecaoCnae = descSecaoCnae;
    }
}