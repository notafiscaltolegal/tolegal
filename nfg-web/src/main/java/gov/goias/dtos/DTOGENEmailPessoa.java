package gov.goias.dtos;

import java.io.Serializable;

/**
 * Created by bruno-cff on 07/12/2015.
 */
public class DTOGENEmailPessoa implements Serializable {

    private Integer tipo;
    private String descricao;

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
