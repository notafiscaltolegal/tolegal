package gov.goias.entidades;


import gov.goias.persistencia.GenericRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by thiago-mb on 15/09/2014.
 */
@Entity
@Table(name = "GEN_CFOP")
@Repository
public class GENCfop extends GenericRepository<Integer, GENCfop> implements Serializable {

    public static final long serialVersionUID = -6541614618845655612L;

    public Integer getCodigoCFOP() {
        return codigoCFOP;
    }

    public void setCodigoCFOP(Integer codigoCFOP) {
        this.codigoCFOP = codigoCFOP;
    }

    public String getDescricaoCFOP() {
        return descricaoCFOP;
    }

    public void setDescricaoCFOP(String descricaoCFOP) {
        this.descricaoCFOP = descricaoCFOP;
    }

    public String getDataInicialVigencia() {
        return dataInicialVigencia;
    }

    public void setDataInicialVigencia(String dataInicialVigencia) {
        this.dataInicialVigencia = dataInicialVigencia;
    }

    public String getDataFinalVigencia() {
        return dataFinalVigencia;
    }

    public void setDataFinalVigencia(String dataFinalVigencia) {
        this.dataFinalVigencia = dataFinalVigencia;
    }

    @Id
    @Column(name = "CODG_CFOP")
    private Integer codigoCFOP;

    @Column(name = "DESC_CFOP")
    private String descricaoCFOP;

    @Column(name = "DATA_INICIAL_VIGENCIA")
    private String dataInicialVigencia;

    @Column(name = "DATA_FINAL_VIGENCIA")
    private String dataFinalVigencia;

}
