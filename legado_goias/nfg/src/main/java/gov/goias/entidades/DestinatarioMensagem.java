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
@Table(name = "NFG_DESTINATARIO_MENSAGEM_NFG")
public class DestinatarioMensagem implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_DEST_MENSAGEM_NFG")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA_DEST_MENSAGEM")
    private GENPessoa genPessoa;

    @ManyToOne
    @JoinColumn(name = "ID_MENSAGEM_NFG")
    private Mensagem mensagem;

    @Column(name = "INDI_LEITURA_CONTADOR")
    private Character leituraContador;


    @Column(name = "DATA_PRIMEIRA_LEITURA_MENSAGEM")
    private Date dataLeitura;


    public Character getLeituraContador() {
        return leituraContador;
    }

    public void setLeituraContador(Character leituraContador) {
        this.leituraContador = leituraContador;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public GENPessoa getGenPessoa() {
        return genPessoa;
    }

    public void setGenPessoa(GENPessoa genPessoa) {
        this.genPessoa = genPessoa;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
}
