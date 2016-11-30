package entidade;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_usr_to_legl")
public class UsuarioToLegal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -213797317396640166L;

	@Id
	@GeneratedValue(generator = "seq_usr_to_legl", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "seq_usr_to_legl", sequenceName = "seq_usr_to_legl",allocationSize=1)
	@Column(name = "id_usr_to_legl")
	private Long id;

	@Column(name="recebe_email")
	private Boolean recebeEmail;

	@Column(name="participa_sorteio")
	private Boolean participaSorteio;
	
	@Column(name = "senha")
	private String senha;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "hash_ativacao")
	private String hash;
	
	@Column(name = "situacao")
	@Enumerated(EnumType.STRING)
	private SituacaoUsuario situacao;
	
	@Column(name = "telefone")
	private String telefone;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_ativacao")
	private Date dataAtivacao;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(referencedColumnName="id_pes_fisica")
	private PessoaFisicaToLegal pessoaFisica;
	
	
	
	@PrePersist
	public void pontuacaoExtra(){
		
		
	}
	
	public Character getRecebeEmailFormat(){
		return booleanToChar(this.getRecebeEmail());
	}
	
	public Character getParticipaSorteioFormat(){
		return booleanToChar(this.getParticipaSorteio());
	}
	
	private char booleanToChar(Boolean bol) {
		
		if (bol == null){
			return 'N';
		}
		
		return bol ? 'S' : 'N';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getRecebeEmail() {
		return recebeEmail;
	}

	public void setRecebeEmail(Boolean recebeEmail) {
		this.recebeEmail = recebeEmail;
	}

	public Boolean getParticipaSorteio() {
		return participaSorteio;
	}

	public void setParticipaSorteio(Boolean participaSorteio) {
		this.participaSorteio = participaSorteio;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public PessoaFisicaToLegal getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisicaToLegal pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public SituacaoUsuario getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoUsuario situacao) {
		this.situacao = situacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}
}