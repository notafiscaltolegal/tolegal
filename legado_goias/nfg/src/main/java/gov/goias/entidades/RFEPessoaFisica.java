package gov.goias.entidades;

import gov.goias.persistencia.GENPessoaFisicaRepository;
import gov.goias.persistencia.RFEPessoaFisicaRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lucas-mp
 * Date: 23/10/14
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Repository
@Table(name = "RFE_PESSOA_FISICA_RF")
public class RFEPessoaFisica extends RFEPessoaFisicaRepository implements Serializable {
	private static final long serialVersionUID = -5679262923135613774L;

    @Id
    @Column(name = "NUMR_CPF")
    @Size(min = 11,max = 11,message = "O CPF deve conter 11 digitos")
    @NotNull
    private String cpf;

    @Column(name = "NOME_PESSOA")
    private String nome;

    @Column(name = "DATA_NASCIMENTO")
    @Past(message = "Data de nascimento deve ser anterior à data atual")
    @NotNull(message = "Informe a data de nascimento")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date dataDeNascimento;

    @Column(name = "NOME_MAE")
    @Size(min = 10, message = "Nome da mãe deve ter ao menos 10 caracteres")
    @NotNull(message = "Informe o nome da mãe")
    private String nomeDaMae;

    @Override
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    @Override
    public Integer getIdPessoa() {
        return null;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    @Override
    public String getNomeDaMae() {
        return nomeDaMae;
    }

    public void setNomeDaMae(String nomeDaMae) {
        this.nomeDaMae = nomeDaMae;
    }
}