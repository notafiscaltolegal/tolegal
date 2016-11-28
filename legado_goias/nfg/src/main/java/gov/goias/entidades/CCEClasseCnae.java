package gov.goias.entidades;

import gov.goias.persistencia.CCEClasseCnaeRepository;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by thiago-mb on 23/07/2014.
 */
@Entity
@Table(name = "CCE_CLASSE_CNAE_FISCAL")
@Repository
@Cacheable(true)
public class CCEClasseCnae  extends CCEClasseCnaeRepository implements Serializable {

    private static final long serialVersionUID = -564564646465465465L;

    @Id
    @Column(name = "ID_CLASSE_CNAEF")
    private Integer idClasseCnae;

    @Column(name = "CODG_CLASSE_CNAEF")
    private String codClasseCnae;

    @Column(name = "STAT_CLASSE_CNAEF")
    private Character statClasseCnae;

    @Column (name = "DESC_CLASSE_CNAEF", nullable = false)
    private String descClasseCnae;

    @ManyToOne
    @JoinColumn(name = "ID_GRUPO_CNAEF")
    @Fetch(value = FetchMode.JOIN)
    private CCEGrupoCnae grupoCnae;

    public long getSerialVersionUID(){return serialVersionUID;}

    public Integer getIdClasseCnae(){return idClasseCnae;}

    public void setIdClasseCnae(Integer idClasseCnae){
        this.idClasseCnae = idClasseCnae;
    }

    public String getCodClasseCnae(){return codClasseCnae;}

    public void setCodClasseCnae(String codClasseCnae){
        this.codClasseCnae = codClasseCnae;
    }

    public Character getStatClasseCnae(){return statClasseCnae;}

    public void setStatClasseCnae(Character statClasseCnae){
        this.statClasseCnae = statClasseCnae;
    }

    public CCEGrupoCnae getGrupoCnae(){return grupoCnae;}

    public void setGrupoCnae(CCEGrupoCnae grupoCnae){
        this.grupoCnae = grupoCnae;
    }

    public String getDescClasseCnae() { return descClasseCnae; }

    public void setDescClasseCnae(String descClasseCnae) { this.descClasseCnae = descClasseCnae; }
}
