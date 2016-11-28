package gov.goias.entidades;


import gov.goias.persistencia.CfopAutorizadoRepository;
import gov.goias.persistencia.CnaeAutorizadoRepository;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "NFG_CFOP_AUTORIZADO")
@Repository
public class CfopAutorizado extends CfopAutorizadoRepository implements Serializable {
    private static final long serialVersionUID = 958538797256100278L;

    @Id
    @Column(name = "ID_CFOP_AUTORIZADO")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer idCfopeAutorizado;

    @Column(name = "DATA_INCLUSAO_CFOP")
    private Date dataInclusaoCfop;

    @ManyToOne
    @JoinColumn(name = "CODG_CFOP")
    private GENCfop genCfop;

    @Column(name = "DATA_EXCLUSAO_CFOP")
    private Date dataExclusaoCfop;

    public Integer getIdCfopeAutorizado() {
        return idCfopeAutorizado;
    }

    public void setIdCfopeAutorizado(Integer idCfopeAutorizado) {
        this.idCfopeAutorizado = idCfopeAutorizado;
    }

    public Date getDataInclusaoCfop() {
        return dataInclusaoCfop;
    }

    public void setDataInclusaoCfop(Date dataInclusaoCfop) {
        this.dataInclusaoCfop = dataInclusaoCfop;
    }

    public GENCfop getGenCfop() {
        return genCfop;
    }

    public void setGenCfop(GENCfop genCfop) {
        this.genCfop = genCfop;
    }

    public Date getDataExclusaoCfop() {
        return dataExclusaoCfop;
    }

    public void setDataExclusaoCfop(Date dataExclusaoCfop) {
        this.dataExclusaoCfop = dataExclusaoCfop;
    }


    @Override
    public List<CfopAutorizado> listCfopsAutorizadosNaoExcluidos(Map<String, Object> params) {
        return super.listCfopsAutorizadosNaoExcluidos(params);
    }
}