package gov.goias.entidades;

import gov.goias.persistencia.BilhetePessoaRepository;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by bruno-cff on 06/05/2015.
 */
@Entity
@Table(name = "NFG_PREMIO_SORTEIO")
@Repository
public class PremioSorteio extends GenericRepository<Integer,PremioSorteio> implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_PREMIO_SORTEIO")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "DESC_PREMIO")
    private String descricao;

    @Column(name = "VALR_PREMIO")
    private Integer valor;

    @Column(name = "QTDE_PREMIO")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "ID_REGRA_SORTEIO")
    private RegraSorteio regraSorteio;

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

    public Integer getValor() {
        return valor;
    }

    public String getValorFormatado() {
        DecimalFormat formatter = new DecimalFormat("R$ ###,###,###.00", new DecimalFormatSymbols(new Locale("pt","BR")));
        return formatter.format(valor);
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public RegraSorteio getRegraSorteio() {
        return regraSorteio;
    }

    public void setRegraSorteio(RegraSorteio regraSorteio) {
        this.regraSorteio = regraSorteio;
    }
}
