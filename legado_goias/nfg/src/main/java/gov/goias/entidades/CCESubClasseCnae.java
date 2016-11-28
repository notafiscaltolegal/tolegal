package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Repository;
import gov.goias.persistencia.CCESubClasseCnaeRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thiago-mb on 23/07/2014.
 */
@Entity
@Table(name = "CCE_SUBCLASSE_CNAE_FISCAL")
@Repository
@Cacheable(true)
public class CCESubClasseCnae extends CCESubClasseCnaeRepository implements Serializable {

    public static final long serialVersionUID = -6541614618845655612L;

    @Id
    @Column(name = "ID_SUBCLASSE_CNAEF")
    private Long idSubClasseCnae;

    @Column(name = "CODG_SUBCLASSE_CNAEF")
    private String codSubClasseCnae;

    @Column(name = "STAT_SUBCLASSE_CNAEF")
    private Character statSubClasseCnae;

    @Column(name = "DESC_SUBCLASSE_CNAEF")
    private String descSubClasseCnae;

    @ManyToOne
    @JoinColumn(name = "ID_CLASSE_CNAEF")
    @Fetch(value = FetchMode.JOIN)
    private CCEClasseCnae classeCnae;

    public long getSerialVersionUID(){return serialVersionUID;}

    public Long getIdSubClasseCnae(){return idSubClasseCnae;}

    public void setIdSubClasseCnae(Long idSubClasseCnae){
        this.idSubClasseCnae = idSubClasseCnae;
    }

    public String getCodSubClasseCnae(){return codSubClasseCnae;}

    public void setCodSubClasseCnae(String codSubClasseCnae){
        this.codSubClasseCnae = codSubClasseCnae;
    }

    public Character getStatSubClasseCnae(){return statSubClasseCnae;}

    public void setStatSubClasseCnae(Character statSubClasseCnae){
        this.statSubClasseCnae = statSubClasseCnae;
    }

    public CCEClasseCnae getClasseCnae(){return classeCnae;}

    public void setClasseCnae(CCEClasseCnae classeCnae){
        this.classeCnae = classeCnae;
    }

    public String getDescSubClasseCnae() { return descSubClasseCnae; }

    public void setDescSubClasseCnae(String descSubClasseCnae) { this.descSubClasseCnae = descSubClasseCnae; }

}
