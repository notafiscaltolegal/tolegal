package gov.goias.dtos;

import java.io.Serializable;

/**
 * Created by bruno-cff on 07/12/2015.
 */
public class DTOGENTelefonePessoa implements Serializable {

    private Integer tipo;
    private Integer ddd;
    private Integer numero;

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
