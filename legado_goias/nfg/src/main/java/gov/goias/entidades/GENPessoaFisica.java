package gov.goias.entidades;

import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.GENPessoaFisicaRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Repository
@Table(name = "GEN_PESSOA_FISICA")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable(true)
public class GENPessoaFisica extends GENPessoaFisicaRepository implements Serializable {

    private static final long serialVersionUID = -5679262923135613774L;

    public static final Character PESSOA_FISICA = Character.valueOf('F');
    public static final Character PESSOA_JURIDICA = Character.valueOf('J');

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    @Column(name = "ID_PESSOA")
    private Integer idPessoa;

    @Column(name = "NUMR_PESSOA_BASE")
    private Long numeroPessoaBase;

    @Column(name = "NOME_PESSOA")
    private String nome;

    @Column(name = "NUMR_CPF")
    @Size(min = 11,max = 11,message = "O CPF deve conter 11 digitos")
    @NotNull
    private String cpf;

    @Column(name = "DATA_NASCIMENTO")
    @Past(message = "Data de nascimento deve anteceder a data atual")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date dataDeNascimento;

    @Column(name = "NOME_MAE")
    private String nomeDaMae;

    public GENPessoaFisica() {}

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Long getNumeroPessoaBase() {
        return numeroPessoaBase;
    }

    public void setNumeroPessoaBase(Long numeroPessoaBase) {
        this.numeroPessoaBase = numeroPessoaBase;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        if (nomeDaMae != null && nomeDaMae.length()>0){
            if (nomeDaMae.split(" ").length < 2){
                //Isso ocasionará BindingException nos controllers, tratado pelo GlobalExceptionController
                throw new NFGException("O nome da mãe deve ter no mínimo dois nomes!");
            }else {
                this.nomeDaMae = nomeDaMae;
            }
        }
    }

    public String getMatricula() {
        Long matricula = getNumeroPessoaBase();
        return matricula != null ? matricula.toString() : null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}