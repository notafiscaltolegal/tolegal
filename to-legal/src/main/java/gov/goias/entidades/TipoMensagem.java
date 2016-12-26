package gov.goias.entidades;

import java.io.Serializable;

/**
 * Created by lucas-mp.
 */

public class TipoMensagem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
