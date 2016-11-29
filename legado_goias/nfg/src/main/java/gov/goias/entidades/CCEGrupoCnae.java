package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Repository;
import gov.goias.persistencia.CCEGrupoCnaeRepository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by thiago-mb on 23/07/2014.
 */
@Entity
@Table(name = "CCE_GRUPO_CNAE_FISCAL")
@Repository
@Cacheable(true)
public class CCEGrupoCnae extends CCEGrupoCnaeRepository implements Serializable {

    private static final long serialVersionUID = -5679428310518524973L;

    @Id
    @Column(name = "ID_GRUPO_CNAEF", nullable = false)
    private Integer idGrupoCnae;

    @Column(name = "CODG_GRUPO_CNAEF", nullable = false)
    private String codGrupoCnae;

    @Column(name = "STAT_GRUPO_CNAEF", nullable = false)
    private Character statGrupoCnae;

    @Column (name = "DESC_GRUPO_CNAEF", nullable = false)
    private String descGrupoCnae;

    @ManyToOne
    @JoinColumn(name = "ID_DIVISAO_CNAEF")
    @Fetch(value = FetchMode.JOIN)
    private CCEDivisaoCnae divisaoCnae;

    public long getSerialVersionUID(){return serialVersionUID;}
    
    public Integer getIdGrupoCnae(){return idGrupoCnae;}

    public void setIdGrupoCnae(Integer idGrupoCnae){
        this.idGrupoCnae = idGrupoCnae;
    }

    public String getCodGrupoCnae(){return codGrupoCnae;}

    public void setCodGrupoCnae(String codGrupoCnae){
        this.codGrupoCnae = codGrupoCnae;
    }

    public Character getStatGrupoCnae(){return statGrupoCnae;}

    public void setStatGrupoCnae(Character statGrupoCnae){
        this.statGrupoCnae = statGrupoCnae;
    }

    public CCEDivisaoCnae getDivisaoCnae(){return divisaoCnae;}

    public void setDivisaoCnae(CCEDivisaoCnae divisaoCnae){
        this.divisaoCnae = divisaoCnae;
    }

    public String getDescGrupoCnae() { return descGrupoCnae; }

    public void setDescGrupoCnae(String descGrupoCnae) { this.descGrupoCnae = descGrupoCnae; }
}