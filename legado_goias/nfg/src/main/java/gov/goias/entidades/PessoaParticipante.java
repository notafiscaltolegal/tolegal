package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import gov.goias.persistencia.PessoaParticipanteRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Remisson-ss on 14/10/2014.
 */

@Entity
@Table(name = "NFG_PESSOA_PARTICIPANTE")
@Repository
@Cacheable(true)
public class PessoaParticipante  extends PessoaParticipanteRepository implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Autowired
    @Transient
    SimpleDateFormat simpleDateFormat;

    @Id
    @Column(name = "ID_PESSOA_PARTCT")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "INDI_PARTICIPA_SORTEIO")
    private Character participaSorteio;

    @Column(name = "INDI_RECEBE_EMAIL")
    private Character recebeEmail;

    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;

    @Transient
    public String dataCadastroStr;

    @Transient
    public String emailPreCadastro;
    @Transient
    public String telefonePreCadastro;
    @Transient
    public String nomeMaePreCadastro;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA", unique = true)
    GENPessoaFisica genPessoaFisica;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getRecebeEmail() {
        return recebeEmail;
    }

    public void setRecebeEmail(Character recebeEmail) {
        this.recebeEmail = recebeEmail;
    }

    public Character getParticipaSorteio() {
        return participaSorteio;
    }

    public String getParticipaSorteioString() {
        if (participaSorteio!=null){
            if (participaSorteio.toString().equals("S")){
                return "Sim";
            }else{
                return "Nao";
            }
        }else{
            return null;
        }
    }

    public String getNomeMaePreCadastro() {
        return nomeMaePreCadastro;
    }

    public void setNomeMaePreCadastro(String nomeMaePreCadastro) {
        this.nomeMaePreCadastro = nomeMaePreCadastro;
    }

    public String getRecebeEmailString() {
        if (recebeEmail !=null){

            if (recebeEmail.toString().equals("S")){
                return "Sim";
            }else{
                return "Nao";
            }
        }else {
            return null;
        }
    }

    public void setParticipaSorteio(Character participaSorteio) {
        this.participaSorteio = participaSorteio;
    }

    public GENPessoaFisica getGenPessoaFisica() {
        return genPessoaFisica;
    }

    public void setGenPessoaFisica(GENPessoaFisica genPessoaFisica) {
        this.genPessoaFisica = genPessoaFisica;
    }

    public String getEmailPreCadastro() {
        return emailPreCadastro;
    }

    public void setEmailPreCadastro(String emailPreCadastro) {
        this.emailPreCadastro = emailPreCadastro;
    }

    public String getTelefonePreCadastro() {
        return telefonePreCadastro;
    }

    public void setTelefonePreCadastro(String telefonePreCadastro) {
        this.telefonePreCadastro = telefonePreCadastro;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataCadastroStr() {
        return getDataCadastro()!=null?simpleDateFormat.format(getDataCadastro()):"";
    }

    public void setDataCadastroStr(String dataCadastroStr) {
        this.dataCadastroStr = dataCadastroStr;
    }

    public boolean getExcluidoDosSorteios(){
        return super.cidadaoEstahExcluidoDosSorteios(this);
    }

}
