package gov.to.entidade;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_pes_fisica")
public class PessoaFisicaToLegal extends EntidadeBasica{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_pes_fisica", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_pes_fisica", sequenceName = "seq_pes_fisica",allocationSize=1)
	@Column(name = "id_pes_fisica")
	private Long id;

	@Column(name="cpf")
	private String cpf;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "nome_mae")
	private String nomeMae;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nasc")
	private Date dataNascimento;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(referencedColumnName="id_endereco")
	private EnderecoToLegal endereco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoToLegal getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoToLegal endereco) {
		this.endereco = endereco;
	}
}