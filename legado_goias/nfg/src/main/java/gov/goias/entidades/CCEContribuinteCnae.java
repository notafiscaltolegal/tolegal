package gov.goias.entidades;

import gov.goias.persistencia.CCEContribuinteCnaeRepository;
import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CCE_CONTRIB_CNAEF")
@Repository
public class CCEContribuinteCnae extends CCEContribuinteCnaeRepository implements Serializable {

	private static final long serialVersionUID = 919648854256091298L;

	@Id
	@Column(name = "ID_CONTRIB_CNAEF")
	private Integer id;

    @Column(name = "INDI_PRINCIPAL")
    private Character indiPrincipal;

    @ManyToOne
    @JoinColumn(name = "ID_SUBCLASSE_CNAEF")
    private CCESubClasseCnae subClasseCnae;

    @ManyToOne
    @JoinColumn(name = "NUMR_INSCRICAO")
    private CCEContribuinte contribuinte;

    public CCESubClasseCnae getSubClasseCnae() {
        return subClasseCnae;
    }

    public CCEContribuinte getContribuinte() {
        return contribuinte;
    }

    public Boolean isPrincipal() {
        return this.indiPrincipal == 'S';
    }
}
