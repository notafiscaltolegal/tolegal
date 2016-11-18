package gov.goias.entidades;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GEN_PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable(true)
public class GENPessoa implements Serializable {

    private static final long serialVersionUID = -5679262923135613774L;

    public static final Character PESSOA_FISICA = Character.valueOf('F');
    public static final Character PESSOA_JURIDICA = Character.valueOf('J');

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.sefaz.util.hibernate.TriggerAssignedIdentityGenerator")
    @Column(name = "ID_PESSOA")
    private Integer idPessoa;

    @Column(name = "TIPO_PESSOA")
    private Character tipoPessoa;

    @Transient
    private String pessoaDescricao;

    @Transient
    private GENPessoaJuridica pessoaJuridica;

    @Transient
    private GENPessoaFisica pessoaFisica;

    public GENPessoa() {

    }

    public GENPessoa(GENPessoaFisica pessoaFisica) {
        this.idPessoa = pessoaFisica.getIdPessoa();
        this.tipoPessoa = PESSOA_FISICA;
        this.pessoaFisica = pessoaFisica;
    }

    public GENPessoa(GENPessoaJuridica pessoaJuridica) {
        this.idPessoa = pessoaJuridica.getIdPessoa();
        this.tipoPessoa = PESSOA_JURIDICA;
        this.pessoaJuridica = pessoaJuridica;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Character getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(Character tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getPessoaDescricao() {
        return pessoaDescricao;
    }

    public GENPessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(GENPessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public GENPessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(GENPessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }
}