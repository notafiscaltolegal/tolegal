package gov.goias.dtos;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by diogo-rs on 8/11/2014.
 * <NFe>
 *     <id></id>
 *     <cpf_dest></cpf_dest>
 *     <valor_total></valor_total>
 * </NFe>
 */
@XStreamAlias("NFe")
public class DTONFEIdentificacao implements Serializable{

    @XStreamAlias("id")
    private Long id;

    @XStreamAlias("cpf_dest")
    private String cpf;

    @XStreamAlias("valor_total")
    private Double valorTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
