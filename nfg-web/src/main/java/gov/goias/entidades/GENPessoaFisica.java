package gov.goias.entidades;

import java.io.Serializable;
import java.util.Date;

import gov.goias.exceptions.NFGException;

public class GENPessoaFisica implements Serializable {

    private static final long serialVersionUID = -5679262923135613774L;

    public static final Character PESSOA_FISICA = Character.valueOf('F');
    public static final Character PESSOA_JURIDICA = Character.valueOf('J');

    private Integer idPessoa;

    private Long numeroPessoaBase;

    private String nome;

    private String cpf;

    private Date dataDeNascimento;

    private String nomeDaMae;
    
    private String telefone;
    
    private String email;

    public GENPessoaFisica() {}
    
    public String getEmailWs(){
        return this.email;
    }

    public String getTelefoneWs(){
       return this.telefone;
    }

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}