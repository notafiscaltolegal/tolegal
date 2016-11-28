package gov.goias.entidades;

import gov.goias.persistencia.MensagemRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lucas-mp.
 */

@Entity
@Table(name = "NFG_TIPO_MENSAGEM_NFG")
public class TipoMensagem implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODG_TIPO_MENSAGEM")
    private Integer id;

    @Column(name = "NOME_TIPO_MENSAGEM")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
