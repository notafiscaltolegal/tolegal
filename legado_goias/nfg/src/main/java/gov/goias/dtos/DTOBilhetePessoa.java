package gov.goias.dtos;

import java.io.Serializable;

/**
 * Created by bruno-cff on 15/05/2015.
 */
public class DTOBilhetePessoa implements Serializable {

    private String premiado;
    private Integer idBilhete;
    private Integer numero;
    private Integer idSorteio;
    private Integer idPessoa;
    private String bilheteDefinitivo;

    public String getPremiado() {
        return premiado;
    }

    public void setPremiado(String premiado) {
        this.premiado = premiado;
    }

    public Integer getIdBilhete() {
        return idBilhete;
    }

    public void setIdBilhete(Integer idBilhete) {
        this.idBilhete = idBilhete;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getIdSorteio() {
        return idSorteio;
    }

    public void setIdSorteio(Integer idSorteio) {
        this.idSorteio = idSorteio;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getBilheteDefinitivo() {
        return bilheteDefinitivo;
    }

    public void setBilheteDefinitivo(String bilheteDefinitivo) {
        this.bilheteDefinitivo = bilheteDefinitivo;
    }
}
