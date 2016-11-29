package gov.goias.entidades;

import gov.goias.persistencia.MensagemRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by lucas-mp.
 */

@Entity
@Table(name = "NFG_MENSAGEM_NFG")
@Repository
@Cacheable(true)
public class Mensagem extends MensagemRepository implements Serializable, gov.goias.entidades.JdbcMappable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_MENSAGEM_NFG")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "TIPO_DEST_MENSAGEM_NFG")
    private Character tipoDestinatario;

    @Column(name = "INDI_MENSAGEM_PUBLICA")
    private Character mensagemPublica;

    @Column(name = "DESC_TITULO_MENSAGEM")
    private String titulo;

    @Column(name = "DESC_MENSAGEM")
    private String texto;

    @Column(name = "DATA_MENSAGEM")
    private Date data;

    @Transient
    private String listaDestinatariosString;

    @ManyToOne
    @JoinColumn(name = "CODG_TIPO_MENSAGEM")
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


    @Override
    public void populate(ResultSet resultSet) throws SQLException {
        setTexto(resultSet.getString("DESC_MENSAGEM"));
        setTitulo(resultSet.getString("DESC_TITULO_MENSAGEM"));
        setData(resultSet.getTimestamp("DATA_MENSAGEM"));
        setMensagemPublica(resultSet.getString("INDI_MENSAGEM_PUBLICA").charAt(0));
        setTipoDestinatario(resultSet.getString("TIPO_DEST_MENSAGEM_NFG").charAt(0));
    }
}