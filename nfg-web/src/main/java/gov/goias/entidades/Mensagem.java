package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lucas-mp.
 */

public class Mensagem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Character tipoDestinatario;

    private Character mensagemPublica;

    private String titulo;

    private String texto;

    private Date data;

    private String listaDestinatariosString;

    private TipoMensagem tipoMensagem;

    public Mensagem(Character tipoDestinatario, Character mensagemPublica, String titulo, String texto, Date data) {
        this.tipoDestinatario = tipoDestinatario;
        this.mensagemPublica = mensagemPublica;
        this.titulo = titulo;
        this.texto = texto;
        this.data = data;
    }

    public Mensagem() {
    }

    public Character getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(Character tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public Character getMensagemPublica() {
        return mensagemPublica;
    }

    public void setMensagemPublica(Character mensagemPublica) {
        this.mensagemPublica = mensagemPublica;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getListaDestinatariosString() {
        return listaDestinatariosString;
    }

    public void setListaDestinatariosString(String listaDestinatariosString) {
        this.listaDestinatariosString = listaDestinatariosString;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
