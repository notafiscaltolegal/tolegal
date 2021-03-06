package gov.to.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import gov.to.persistencia.EntidadeBasica;

@Entity
@Table(name = "tb_endereco")
public class EnderecoToLegal extends EntidadeBasica{
	
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_endereco", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq_endereco", sequenceName = "seq_endereco",allocationSize=1)
	@Column(name = "id_endereco")
	private Long id;

	@Column(name="cep")
	private String cep;
	
	@Column(name = "logradouro")
	private String logradouro;
	
	@Column(name = "numero")
	private String numero;

	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "complemento")
	private String complemento;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="munibge")
	private MunicipioToLegal municipio;
	
	public Long getId() {
		return id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public MunicipioToLegal getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioToLegal municipio) {
		this.municipio = municipio;
	}

	public void setId(Long id) {
		this.id = id;
	}
}