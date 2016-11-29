package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Repository;
import gov.goias.persistencia.CCEDivisaoCnaeRepository;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by thiago-mb on 23/07/2014.
 */
@Entity
@Table(name = "CCE_DIVISAO_CNAE_FISCAL")
@Repository
@Cacheable(true)
public class CCEDivisaoCnae extends CCEDivisaoCnaeRepository implements Serializable {
    private static final long serialVersionUID = -7196317072508726180L;
    @Id
    @Column(name = "ID_DIVISAO_CNAEF", nullable = false)
    private Integer idDivisaoCnae;

    @Column(name = "CODG_DIVISAO_CNAEF", nullable = false)
    private String codDivisaoCnae;

    @Column(name = "STAT_DIVISAO_CNAEF", nullable = false)
    private Character statDivisaoCnae;

    @Column (name = "DESC_DIVISAO_CNAEF", nullable = false)
    private String descDivisaoCnae;

    @ManyToOne
    @JoinColumn(name = "ID_SECAO_CNAEF")
    @Fetch(value = FetchMode.JOIN)
    private CCESecaoCnae secaoCnae;


    public long getSerialVersionUID(){return serialVersionUID;}

    public Integer getIdDivisaoCnae(){return idDivisaoCnae;}

    public void setIdDivisaoCnae(Integer idDivisaoCnae){
        this.idDivisaoCnae = idDivisaoCnae;
    }

    public String getCodDivisaoCnae(){return codDivisaoCnae;}

    public void setCodDivisaoCnae(String codDivisaoCnae){
        this.codDivisaoCnae = codDivisaoCnae;
    }

    public Character getStatDivisaoCnae(){return statDivisaoCnae;}

    public void setStatDivisaoCnae(Character statDivisaoCnae){
        this.statDivisaoCnae = statDivisaoCnae;
    }

    public CCESecaoCnae getIdSecaoCnae(){return secaoCnae;}

    public void setIdSecaoCnaef(CCESecaoCnae secaoCnae){
        this.secaoCnae = secaoCnae;
    }

    public String getDescDivisaoCnae() { return descDivisaoCnae; }

    public void setDescDivisaoCnae(String descDivisaoCnae) { this.descDivisaoCnae = descDivisaoCnae; }
}